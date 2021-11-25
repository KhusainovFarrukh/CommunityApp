package khusainov.farrukh.communityapp.ui.notifications.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.notifications.viewmodel.NotificationsViewModel

/**
 *Created by farrukh_kh on 11/24/21 9:41 PM
 *khusainov.farrukh.communityapp.ui.notifications.di
 **/
@Module
abstract class NotificationsModule {

	@Binds
	@IntoMap
	@ViewModelKey(NotificationsViewModel::class)
	abstract fun bindViewModel(viewModel: NotificationsViewModel): ViewModel
}