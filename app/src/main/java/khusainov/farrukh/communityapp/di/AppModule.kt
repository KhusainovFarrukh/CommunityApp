package khusainov.farrukh.communityapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.utils.AddCookiesInterceptor
import khusainov.farrukh.communityapp.utils.ReceivedCookiesInterceptor
import khusainov.farrukh.communityapp.utils.Constants
import khusainov.farrukh.communityapp.utils.Constants.KEY_COOKIES_REQUEST
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/24/21 8:46 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
object AppModule {

	@Singleton
	@Provides
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
		Retrofit.Builder()
			.baseUrl(Constants.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

	@Singleton
	@Provides
	fun provideSharedPrefs(context: Context): SharedPreferences =
		context.getSharedPreferences(KEY_COOKIES_REQUEST, MODE_PRIVATE)

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
}