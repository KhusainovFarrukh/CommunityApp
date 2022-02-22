package khusainov.farrukh.communityapp.di

import androidx.lifecycle.ViewModel
import dagger.*
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.data.auth.remote.AuthApi
import khusainov.farrukh.communityapp.ui.auth.view.LoginDialogFragment
import khusainov.farrukh.communityapp.ui.auth.viewmodel.LoginViewModel
import khusainov.farrukh.communityapp.ui.home.di.HomeModule
import khusainov.farrukh.communityapp.ui.home.view.HomeFragment
import khusainov.farrukh.communityapp.ui.notifications.di.NotificationsModule
import khusainov.farrukh.communityapp.ui.notifications.view.NotificationsFragment
import retrofit2.Retrofit
import retrofit2.create

/**
 *Created by farrukh_kh on 2/21/22 7:53 PM
 *khusainov.farrukh.communityapp.di
 **/

@Module(includes = [
	HomeActivityModule.HomeFragmentBuildersModule::class,
	HomeActivityModule.HomeActivityViewModelsModule::class
])
object HomeActivityModule {

	@ActivityScope
	@Provides
	fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()

	@Module
	abstract class HomeFragmentBuildersModule {

		@HomeScope
		@ContributesAndroidInjector(modules = [HomeModule::class])
		abstract fun contributeHomeFragment(): HomeFragment

		@NotificationsScope
		@ContributesAndroidInjector(modules = [NotificationsModule::class])
		abstract fun contributeNotificationsFragment(): NotificationsFragment

		@ContributesAndroidInjector
		abstract fun contributeLoginDialogFragment(): LoginDialogFragment
	}

	@Module
	abstract class HomeActivityViewModelsModule {

		@ActivityScope
		@Binds
		@IntoMap
		@ViewModelKey(LoginViewModel::class)
		abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
	}
}

