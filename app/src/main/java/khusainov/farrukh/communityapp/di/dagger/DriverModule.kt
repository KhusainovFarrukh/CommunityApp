package khusainov.farrukh.communityapp.di.dagger

import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.di.car.Driver
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 10/29/21 9:59 AM
 *khusainov.farrukh.communityapp.di.dagger
 **/
@Module
abstract class DriverModule {

	companion object {

		@Singleton
		@Provides
		fun provideDriver() = Driver()
	}
}