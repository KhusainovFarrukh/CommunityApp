package khusainov.farrukh.communityapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import khusainov.farrukh.communityapp.CommunityApp
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 2/18/22 10:05 PM
 *khusainov.farrukh.communityapp.di
 **/
@Singleton
@Component(
	modules = [
		AndroidSupportInjectionModule::class,
		ViewModelFactoryModule::class,
		ActivityBuildersModule::class,
		AppModule::class
	]
)
interface AppComponent : AndroidInjector<CommunityApp> {

	@Component.Factory
	interface Factory {
		fun create(@BindsInstance applicationContext: Context): AppComponent
	}
}