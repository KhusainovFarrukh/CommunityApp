package khusainov.farrukh.communityapp.data.api

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import khusainov.farrukh.communityapp.utils.Constants.Companion.BASE_URL
import khusainov.farrukh.communityapp.utils.Constants.Companion.COOKIES_KEY
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance(context: Context) {

    private val sharedPref = context.getSharedPreferences("COOKIES", MODE_PRIVATE)
    private val editor = sharedPref.edit()

    private val client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(ReceivedCookiesInterceptor())
        .addInterceptor(AddCookiesInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val communityApi: CommunityApi = retrofit.create(CommunityApi::class.java)

    //interceptor to save cookies from every request
    inner class ReceivedCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            chain.proceed(chain.request()).let { response ->
                response.headers(COOKIES_KEY).forEach {
                    val cookie = Cookie.parse(chain.request().url, it)!!
                    editor.putString(cookie.name, it.split(";")[0]).commit()
                    Log.wtf(chain.request().url.toString(), it)
                }
                return response
            }
        }
    }

    //interceptor to add cookies to network request
    inner class AddCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            if (originalRequest.url.toString().contains("votes")) {
                requestBuilder.addHeader(
                    "Cookie",
//                    "userId=5f62486481ef7674fb036d39" + ";" +
                    sharedPref.getString("remember_me", "") + ";" +
                            sharedPref.getString("_csrf", "") + ";" +
                            sharedPref.getString("sessionId", "")
//                            sharedPref.getString("CSRF-Token", "")
                )
                requestBuilder.addHeader(
                    "CSRF-Token",
                    (sharedPref.getString("CSRF-Token", "") ?: "").split("=")[1]
                )
            }

            if (originalRequest.url.toString().contains("notifications")) {
                requestBuilder.addHeader(
                    "Cookie", sharedPref.getString("sessionId", "") + ";" +
                            sharedPref.getString("remember_me", "")
                )
            }

            requestBuilder.build().body.toString()
            return chain.proceed(requestBuilder.build())
        }
    }
}