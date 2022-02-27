package khusainov.farrukh.communityapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import khusainov.farrukh.communityapp.data.auth.remote.AuthApi
import khusainov.farrukh.communityapp.data.comments.remote.CommentsApi
import khusainov.farrukh.communityapp.data.notifications.remote.NotificationsApi
import khusainov.farrukh.communityapp.data.posts.remote.PostsApi
import khusainov.farrukh.communityapp.data.topics.remote.TopicsApi
import khusainov.farrukh.communityapp.data.user.remote.UserApi
import khusainov.farrukh.communityapp.data.utils.api.AddCookiesInterceptor
import khusainov.farrukh.communityapp.data.utils.api.ReceivedCookiesInterceptor
import khusainov.farrukh.communityapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 2/27/22 5:16 PM
 *khusainov.farrukh.communityapp.di
 **/
@InstallIn(SingletonComponent::class)
@Module(includes = [AppModule.ApiModule::class])
object AppModule {

	@Singleton
	@Provides
	fun provideCookiesSharedPrefs(@ApplicationContext context: Context): SharedPreferences =
		context.getSharedPreferences(Constants.KEY_COOKIES_REQUEST, Context.MODE_PRIVATE)

	@Singleton
	@Provides
	fun provideCookiesSharedPrefsEditor(sharedPrefs: SharedPreferences): SharedPreferences.Editor =
		sharedPrefs.edit()

	@Singleton
	@Provides
	fun provideOkHttpClient(
		receivedCookiesInterceptor: ReceivedCookiesInterceptor,
		addCookiesInterceptor: AddCookiesInterceptor,
	): OkHttpClient =
		OkHttpClient.Builder()
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
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
		Retrofit.Builder()
			.baseUrl(Constants.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()


	@InstallIn(SingletonComponent::class)
	@Module
	object ApiModule {

		@Provides
		fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()

		@Provides
		fun provideCommentsApi(retrofit: Retrofit): CommentsApi = retrofit.create()

		@Provides
		fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi = retrofit.create()

		@Provides
		fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

		@Provides
		fun provideTopicsApi(retrofit: Retrofit): TopicsApi = retrofit.create()

		@Provides
		fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create()
	}
}