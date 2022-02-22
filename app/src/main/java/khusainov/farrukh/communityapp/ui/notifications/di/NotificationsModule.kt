package khusainov.farrukh.communityapp.ui.notifications.di

import androidx.lifecycle.ViewModel
import dagger.*
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.data.notifications.remote.NotificationsApi
import khusainov.farrukh.communityapp.di.NotificationsScope
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.notifications.viewmodel.NotificationsViewModel
import retrofit2.Retrofit
import retrofit2.create

/**
 *Created by farrukh_kh on 2/22/22 9:21 AM
 *khusainov.farrukh.communityapp.ui.notifications.di
 **/
@Module(includes = [NotificationsModule.NotificationsViewModelsModule::class])
object NotificationsModule {

	@NotificationsScope
	@Provides
	fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi = retrofit.create()

	@Module
	abstract class NotificationsViewModelsModule {

		@NotificationsScope
		@Binds
		@IntoMap
		@ViewModelKey(NotificationsViewModel::class)
		abstract fun bindNotificationsViewModel(viewModel: NotificationsViewModel): ViewModel
	}
}