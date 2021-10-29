package khusainov.farrukh.communityapp.di.dagger

import dagger.BindsInstance
import dagger.Component
import khusainov.farrukh.communityapp.di.car.Car
import khusainov.farrukh.communityapp.ui.activities.HomeActivity
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 10/7/21 11:55 PM
 *khusainov.farrukh.communityapp.di
 **/
@ActivityScope
@Component(
	dependencies = [AppComponent::class],
	modules = [WheelModule::class, SpecificEngineImplModule::class])
interface ActivityComponent {

	fun getCar(): Car

	fun inject(activity: HomeActivity)

	@Component.Builder
	interface Builder {

		fun build(): ActivityComponent

		fun appComponent(appComponent: AppComponent): Builder

		@BindsInstance
		fun horsePower(@HorsePower horsePower: Int): Builder

		@BindsInstance
		fun fuelConsumePerKm(@FuelConsumePerKm fuelConsumePerKm: Int): Builder
	}
}