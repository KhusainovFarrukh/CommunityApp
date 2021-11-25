package khusainov.farrukh.communityapp.ui.home.di

import dagger.Subcomponent
import khusainov.farrukh.communityapp.ui.home.view.LoginDialogFragment
import khusainov.farrukh.communityapp.ui.home.view.HomeFragment

/**
 *Created by farrukh_kh on 11/24/21 9:39 PM
 *khusainov.farrukh.communityapp.ui.home.di
 **/
@Subcomponent(modules = [HomeModule::class])
interface HomeSubcomponent {

	@Subcomponent.Factory
	interface Factory {
		fun create(): HomeSubcomponent
	}

	fun inject(fragment: HomeFragment)
	fun inject(fragment: LoginDialogFragment)
}