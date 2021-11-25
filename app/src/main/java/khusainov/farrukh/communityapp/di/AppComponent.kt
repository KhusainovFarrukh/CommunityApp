package khusainov.farrukh.communityapp.di

import android.content.Context
import dagger.*
import khusainov.farrukh.communityapp.ui.article_details.di.ArticleDetailsSubcomponent
import khusainov.farrukh.communityapp.ui.home.di.HomeSubcomponent
import khusainov.farrukh.communityapp.ui.notifications.di.NotificationsSubcomponent
import khusainov.farrukh.communityapp.ui.topic_details.di.TopicDetailsSubcomponent
import khusainov.farrukh.communityapp.ui.user.di.UserSubcomponent
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/24/21 8:43 PM
 *khusainov.farrukh.communityapp.di
 **/
@Singleton
@Component(modules = [
	AppModule::class,
	ApiModule::class,
	ViewModelFactoryModule::class,
	SubcomponentsModule::class
])
interface AppComponent {

	@Component.Factory
	interface Factory {
		fun create(@BindsInstance context: Context): AppComponent
	}

	fun articleDetailsSubcomponent(): ArticleDetailsSubcomponent.Factory
	fun homeSubcomponent(): HomeSubcomponent.Factory
	fun notificationsSubcomponent(): NotificationsSubcomponent.Factory
	fun topicDetailsSubcomponent(): TopicDetailsSubcomponent.Factory
	fun userSubcomponent(): UserSubcomponent.Factory
}

@Module(subcomponents = [
	ArticleDetailsSubcomponent::class,
	HomeSubcomponent::class,
	NotificationsSubcomponent::class,
	TopicDetailsSubcomponent::class,
	UserSubcomponent::class
])
object SubcomponentsModule