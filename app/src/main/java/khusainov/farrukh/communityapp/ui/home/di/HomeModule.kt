package khusainov.farrukh.communityapp.ui.home.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.home.viewmodel.LoginViewModel
import khusainov.farrukh.communityapp.ui.home.viewmodel.HomeViewModel

/**
 *Created by farrukh_kh on 11/24/21 9:39 PM
 *khusainov.farrukh.communityapp.ui.home.di
 **/
@Module
abstract class HomeModule {

	@Binds
	@IntoMap
	@ViewModelKey(HomeViewModel::class)
	abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(LoginViewModel::class)
	abstract fun bindViewModel(viewModel: LoginViewModel): ViewModel
}