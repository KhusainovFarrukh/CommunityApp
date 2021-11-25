package khusainov.farrukh.communityapp.ui.user.di

import dagger.BindsInstance
import dagger.Subcomponent
import khusainov.farrukh.communityapp.di.*
import khusainov.farrukh.communityapp.ui.user.view.PostsOfUserFragment
import khusainov.farrukh.communityapp.ui.user.view.UserFragment

/**
 *Created by farrukh_kh on 11/24/21 9:46 PM
 *khusainov.farrukh.communityapp.ui.user.di
 **/
@Subcomponent(modules = [UserModule::class])
interface UserSubcomponent {

	@Subcomponent.Factory
	interface Factory {
		fun create(
			@BindsInstance @UserId userId: String,
			@BindsInstance @Type type: String = "",
			@BindsInstance @SortBy sortBy: String = "",
		): UserSubcomponent
	}

	fun inject(fragment: UserFragment)
	fun inject(fragment: PostsOfUserFragment)
}