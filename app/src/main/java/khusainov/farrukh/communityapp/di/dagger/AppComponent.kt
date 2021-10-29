package khusainov.farrukh.communityapp.di.dagger

import dagger.Component
import khusainov.farrukh.communityapp.di.car.Driver
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 10/29/21 10:54 AM
 *khusainov.farrukh.communityapp.di.dagger
 **/
@Singleton
@Component(modules = [DriverModule::class])
interface AppComponent {

//	fun getDriver(): Driver
	fun getActivityComponentBuilder(): ActivityComponent.Builder
}