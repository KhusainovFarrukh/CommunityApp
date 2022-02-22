package khusainov.farrukh.communityapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.data.auth.remote.SignInRequest
import khusainov.farrukh.communityapp.utils.*
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.random.Random

/**
 *Created by farrukh_kh on 2/19/22 5:30 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
object AppModule {

	@Singleton
	@Provides
	fun provideCookieSharedPrefs(context: Context): SharedPreferences =
		context.getSharedPreferences(Constants.KEY_COOKIES_REQUEST, Context.MODE_PRIVATE)

	@Singleton
	@Provides
	fun provideSharedPrefsEditor(sharedPrefs: SharedPreferences): SharedPreferences.Editor =
		sharedPrefs.edit()

	@Singleton
	@Provides
	fun provideOkHttpClient(
		receivedCookiesInterceptor: ReceivedCookiesInterceptor,
		addCookiesInterceptor: AddCookiesInterceptor,
	): OkHttpClient = OkHttpClient.Builder()
		.connectTimeout(100, TimeUnit.SECONDS)
		.readTimeout(100, TimeUnit.SECONDS)
		.writeTimeout(100, TimeUnit.SECONDS)
		.protocols(listOf(Protocol.HTTP_1_1))
		.addInterceptor(receivedCookiesInterceptor)
		.addInterceptor(addCookiesInterceptor)
		.addInterceptor(HttpLoggingInterceptor().apply {
			setLevel(HttpLoggingInterceptor.Level.BODY)
		})
		.build()

	@Singleton
	@Provides
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
	): Retrofit = Retrofit.Builder()
		.baseUrl(Constants.BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
}