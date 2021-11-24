package khusainov.farrukh.communityapp.di.dagger

import javax.inject.Qualifier

/**
 *Created by farrukh_kh on 10/21/21 11:27 PM
 *khusainov.farrukh.communityapp.di
 **/
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class HorsePower(
	val value: String = "horsePower"
)

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class FuelConsumePerKm(
	val value: String = "fuelConsumePerKm"
)