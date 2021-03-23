package khusainov.farrukh.communityapp.data.api

import android.content.Context
import android.content.Context.MODE_PRIVATE
import khusainov.farrukh.communityapp.utils.Constants.BASE_URL
import khusainov.farrukh.communityapp.utils.Constants.DELIMITER_COOKIES
import khusainov.farrukh.communityapp.utils.Constants.DELIMITER_CSRF
import khusainov.farrukh.communityapp.utils.Constants.KEY_COOKIES_REQUEST
import khusainov.farrukh.communityapp.utils.Constants.KEY_COOKIES_RESPONSE
import khusainov.farrukh.communityapp.utils.Constants.KEY_CSRF
import khusainov.farrukh.communityapp.utils.Constants.KEY_CSRF_TOKEN
import khusainov.farrukh.communityapp.utils.Constants.KEY_REMEMBER_ME
import khusainov.farrukh.communityapp.utils.Constants.KEY_SESSION_ID
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance(context: Context) {

    //SharedPref to save cookies
    private val sharedPref = context.getSharedPreferences(KEY_COOKIES_REQUEST, MODE_PRIVATE)
    private val editor = sharedPref.edit()

    private val client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(ReceivedCookiesInterceptor())
        .addInterceptor(AddCookiesInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val communityApiService: CommunityApiService = retrofit.create(CommunityApiService::class.java)

    //interceptor to save cookies from every request
    inner class ReceivedCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            chain.proceed(chain.request()).let { response ->
                response.headers(KEY_COOKIES_RESPONSE).forEach {
                    val cookie = Cookie.parse(chain.request().url, it)!!
                    editor.putString(cookie.name, it.split(DELIMITER_COOKIES)[0]).commit()
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

            requestBuilder.addHeader(
                KEY_COOKIES_REQUEST,
                sharedPref.getString(KEY_USER_ID, "") + DELIMITER_COOKIES +
                        sharedPref.getString(KEY_REMEMBER_ME, "") + DELIMITER_COOKIES +
                        sharedPref.getString(KEY_CSRF, "") + DELIMITER_COOKIES +
                        sharedPref.getString(KEY_SESSION_ID, "") + DELIMITER_COOKIES +
                        sharedPref.getString(KEY_CSRF_TOKEN, "")
            )
            requestBuilder.addHeader(
                KEY_CSRF_TOKEN,
                (sharedPref.getString(KEY_CSRF_TOKEN, "") ?: "").split(DELIMITER_CSRF)[1]
            )

            return chain.proceed(requestBuilder.build())
        }
    }
}