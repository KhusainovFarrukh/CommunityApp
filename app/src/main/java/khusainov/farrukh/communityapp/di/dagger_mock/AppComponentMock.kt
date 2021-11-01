package khusainov.farrukh.communityapp.di.dagger_mock

import dagger.Component
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 10/29/21 10:54 AM
 *khusainov.farrukh.communityapp.di.dagger
 **/
@Singleton
@Component(modules = [DriverModule::class])
interface AppComponentMock {

//	fun getDriver(): Driver

//	fun getActivityComponentBuilder(): ActivityComponent.Builder

	fun getActivityComponentFactory(): ActivityComponentMock.Factory
}