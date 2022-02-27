package khusainov.farrukh.communityapp.data.utils.api

import android.content.SharedPreferences
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
import javax.inject.Inject

//interceptor to save cookies from every request
class ReceivedCookiesInterceptor @Inject constructor(
	private val editor: SharedPreferences.Editor,
) : Interceptor {

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
class AddCookiesInterceptor @Inject constructor(
	private val sharedPrefs: SharedPreferences,
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {

		val originalRequest = chain.request()
		val requestBuilder = originalRequest.newBuilder()

		requestBuilder.addHeader(
			KEY_COOKIES_REQUEST,
			sharedPrefs.getString(KEY_USER_ID, "") + DELIMITER_COOKIES +
					sharedPrefs.getString(KEY_REMEMBER_ME, "") + DELIMITER_COOKIES +
					sharedPrefs.getString(KEY_CSRF, "") + DELIMITER_COOKIES +
					sharedPrefs.getString(KEY_SESSION_ID, "") + DELIMITER_COOKIES +
					sharedPrefs.getString(KEY_CSRF_TOKEN, "")
		)

		if (sharedPrefs.contains(KEY_CSRF_TOKEN)) {
			(sharedPrefs.getString(KEY_CSRF_TOKEN, "") ?: "").split(DELIMITER_CSRF).let {
				if (it.size > 1) {
					requestBuilder.addHeader(KEY_CSRF_TOKEN, it[1])
				}
			}
		}

		return chain.proceed(requestBuilder.build())
	}
}