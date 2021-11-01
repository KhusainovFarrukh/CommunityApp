package khusainov.farrukh.communityapp.di.modules

import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.data.utils.api.RetrofitInstance
import khusainov.farrukh.communityapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/1/21 12:49 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class RetrofitModule {
	companion object {
		@Singleton
		@Provides
		fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
			.baseUrl(Constants.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

		@Singleton
		@Provides
		fun provideOkHttpClient(
			receivedCookiesInterceptor: RetrofitInstance.ReceivedCookiesInterceptor,
			addCookiesInterceptor: RetrofitInstance.AddCookiesInterceptor,
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
}