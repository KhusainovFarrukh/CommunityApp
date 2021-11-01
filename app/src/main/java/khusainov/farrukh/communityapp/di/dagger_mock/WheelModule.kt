package khusainov.farrukh.communityapp.di.dagger_mock

import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.di.car.*

/**
 *Created by farrukh_kh on 10/18/21 9:36 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class WheelModule {

	companion object {
		@Provides
		fun provideRim() = Rim()

		@Provides
		fun provideTire() = Tire()

		@Provides
		fun provideWheel(tire: Tire, rim: Rim) = Wheel(tire, rim)
	}
}