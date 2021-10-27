package khusainov.farrukh.communityapp.di.dagger

import dagger.BindsInstance
import dagger.Component
import khusainov.farrukh.communityapp.di.car.Car
import khusainov.farrukh.communityapp.ui.activities.HomeActivity

/**
 *Created by farrukh_kh on 10/7/21 11:55 PM
 *khusainov.farrukh.communityapp.di
 **/
@Component(modules = [WheelModule::class, SpecificEngineImplModule::class])
interface CarComponent {

	fun getCar(): Car

	fun inject(activity: HomeActivity)

	@Component.Builder
	interface Builder {

		fun build(): CarComponent

		@BindsInstance
		fun horsePower(@HorsePower horsePower: Int): Builder

		@BindsInstance
		fun fuelConsumePerKm(@FuelConsumePerKm fuelConsumePerKm: Int): Builder
	}
}