package khusainov.farrukh.communityapp.ui.notifications.di

import dagger.Subcomponent
import khusainov.farrukh.communityapp.ui.notifications.view.NotificationsFragment

/**
 *Created by farrukh_kh on 11/24/21 9:41 PM
 *khusainov.farrukh.communityapp.ui.notifications.di
 **/
@Subcomponent(modules = [NotificationsModule::class])
interface NotificationsSubcomponent {

	@Subcomponent.Factory
	interface Factory {
		fun create(): NotificationsSubcomponent
	}

	fun inject(fragment: NotificationsFragment)
}