package khusainov.farrukh.communityapp.di.dagger_mock

import dagger.*
import khusainov.farrukh.communityapp.di.car.Car
import khusainov.farrukh.communityapp.ui.activities.HomeActivity

/**
 *Created by farrukh_kh on 10/7/21 11:55 PM
 *khusainov.farrukh.communityapp.di
 **/
@ActivityScopeMock
@Subcomponent(
//@Component(
//	dependencies = [AppComponent::class],
	modules = [WheelModule::class, SpecificEngineImplModule::class])
interface ActivityComponentMock {

	fun getCar(): Car

	fun inject(activity: HomeActivity)

/*	@Subcomponent.Builder
	interface Builder {

		fun build(): ActivityComponent

		fun horsePower(@BindsInstance @HorsePower horsePower: Int): Builder

		fun fuelConsumePerKm(@BindsInstance @FuelConsumePerKm fuelConsumePerKm: Int): Builder
	}*/

	@Subcomponent.Factory
	interface Factory {

		fun create(@BindsInstance @HorsePower horsePower: Int, @BindsInstance @FuelConsumePerKm fuelConsumePerKm: Int): ActivityComponentMock
	}
}