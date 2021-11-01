package khusainov.farrukh.communityapp.di.modules

import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.data.auth.remote.AuthApi
import khusainov.farrukh.communityapp.data.comments.remote.CommentsApi
import khusainov.farrukh.communityapp.data.notifications.remote.NotificationsApi
import khusainov.farrukh.communityapp.data.posts.remote.PostsApi
import khusainov.farrukh.communityapp.data.topics.remote.TopicsApi
import khusainov.farrukh.communityapp.data.user.remote.UserApi
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/1/21 1:06 PM
 *khusainov.farrukh.communityapp.di.modules
 **/
@Module
abstract class ApiModule {
	companion object {

		@Singleton
		@Provides
		fun provideAuthApi(retrofit: Retrofit): AuthApi =
			retrofit.create(AuthApi::class.java)

		@Singleton
		@Provides
		fun provideCommentsApi(retrofit: Retrofit): CommentsApi =
			retrofit.create(CommentsApi::class.java)

		@Singleton
		@Provides
		fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi =
			retrofit.create(NotificationsApi::class.java)

		@Singleton
		@Provides
		fun providePostsApi(retrofit: Retrofit): PostsApi =
			retrofit.create(PostsApi::class.java)

		@Singleton
		@Provides
		fun provideTopicsApi(retrofit: Retrofit): TopicsApi =
			retrofit.create(TopicsApi::class.java)

		@Singleton
		@Provides
		fun provideUserApi(retrofit: Retrofit): UserApi =
			retrofit.create(UserApi::class.java)
	}
}