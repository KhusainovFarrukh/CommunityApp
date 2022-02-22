package khusainov.farrukh.communityapp.utils

import android.content.SharedPreferences
import okhttp3.*
import javax.inject.Inject

/**
 *Created by farrukh_kh on 2/19/22 9:50 PM
 *khusainov.farrukh.communityapp.utils
 **/
//interceptor to save cookies from every request
class ReceivedCookiesInterceptor @Inject constructor(
	private val editor: SharedPreferences.Editor,
) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		chain.proceed(chain.request()).let { response ->
			response.headers(Constants.KEY_COOKIES_RESPONSE).forEach {
				val cookie = Cookie.parse(chain.request().url, it)!!
				editor.putString(cookie.name, it.split(Constants.DELIMITER_COOKIES)[0]).commit()
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
			Constants.KEY_COOKIES_REQUEST,
			sharedPrefs.getString(Constants.KEY_USER_ID, "") + Constants.DELIMITER_COOKIES +
					sharedPrefs.getString(Constants.KEY_REMEMBER_ME,
						"") + Constants.DELIMITER_COOKIES +
					sharedPrefs.getString(Constants.KEY_CSRF, "") + Constants.DELIMITER_COOKIES +
					sharedPrefs.getString(Constants.KEY_SESSION_ID,
						"") + Constants.DELIMITER_COOKIES +
					sharedPrefs.getString(Constants.KEY_CSRF_TOKEN, "")
		)

		if (sharedPrefs.contains(Constants.KEY_CSRF_TOKEN)) {
			(sharedPrefs.getString(Constants.KEY_CSRF_TOKEN, "")
				?: "").split(Constants.DELIMITER_CSRF).let {
				if (it.size > 1) {
					requestBuilder.addHeader(Constants.KEY_CSRF_TOKEN, it[1])
				}
			}
		}

		return chain.proceed(requestBuilder.build())
	}
}