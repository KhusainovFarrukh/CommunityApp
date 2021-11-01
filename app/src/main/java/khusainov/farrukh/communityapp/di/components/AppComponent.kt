package khusainov.farrukh.communityapp.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import khusainov.farrukh.communityapp.di.modules.*
import khusainov.farrukh.communityapp.ui.activities.HomeActivity
import khusainov.farrukh.communityapp.ui.article_details.view.ArticleFragment
import khusainov.farrukh.communityapp.ui.article_details.view.ReportDialogFragment
import khusainov.farrukh.communityapp.ui.auth.view.LoginDialogFragment
import khusainov.farrukh.communityapp.ui.home.view.HomeFragment
import khusainov.farrukh.communityapp.ui.notifications.view.NotificationsFragment
import khusainov.farrukh.communityapp.ui.topic_details.view.TopicFragment
import khusainov.farrukh.communityapp.ui.user.view.PostsOfUserFragment
import khusainov.farrukh.communityapp.ui.user.view.UserFragment
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/1/21 8:45 PM
 *khusainov.farrukh.communityapp.di.components
 **/
@Singleton
@Component(modules = [ApiModule::class, RetrofitModule::class, SharedPrefsModule::class])
interface AppComponent {

	fun getContext(): Context

	fun inject(fragment: ArticleFragment)
	fun inject(fragment: ReportDialogFragment)
	fun inject(fragment: LoginDialogFragment)
	fun inject(fragment: HomeFragment)
	fun inject(fragment: NotificationsFragment)
	fun inject(fragment: TopicFragment)
	fun inject(fragment: PostsOfUserFragment)
	fun inject(fragment: UserFragment)

	@Component.Factory
	interface Factory {
		fun create(@BindsInstance context: Context): AppComponent
	}
}