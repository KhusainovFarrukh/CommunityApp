package khusainov.farrukh.communityapp.ui.user.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.user.viewmodel.PostsOfUserViewModel
import khusainov.farrukh.communityapp.ui.user.viewmodel.UserViewModel

/**
 *Created by farrukh_kh on 11/24/21 9:45 PM
 *khusainov.farrukh.communityapp.ui.user.di
 **/
@Module
abstract class UserModule {

	@Binds
	@IntoMap
	@ViewModelKey(UserViewModel::class)
	abstract fun bindUserViewModel(viewModel: UserViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(PostsOfUserViewModel::class)
	abstract fun bindPostsOfUserViewModel(viewModel: PostsOfUserViewModel): ViewModel
}