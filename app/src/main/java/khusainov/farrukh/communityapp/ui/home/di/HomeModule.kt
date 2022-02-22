package khusainov.farrukh.communityapp.ui.home.di

import androidx.lifecycle.ViewModel
import dagger.*
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.data.posts.remote.PostsApi
import khusainov.farrukh.communityapp.data.topics.remote.TopicsApi
import khusainov.farrukh.communityapp.di.HomeScope
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.home.viewmodel.HomeViewModel
import retrofit2.Retrofit
import retrofit2.create

/**
 *Created by farrukh_kh on 2/21/22 7:55 PM
 *khusainov.farrukh.communityapp.ui.home.di
 **/
@Module(includes = [HomeModule.HomeViewModelsModule::class])
object HomeModule {

	@HomeScope
	@Provides
	fun provideTopicsApi(retrofit: Retrofit): TopicsApi = retrofit.create()

	@HomeScope
	@Provides
	fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

	@Module
	abstract class HomeViewModelsModule {

		@HomeScope
		@Binds
		@IntoMap
		@ViewModelKey(HomeViewModel::class)
		abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
	}
}