package khusainov.farrukh.communityapp.di.dagger_mock

import dagger.*
import khusainov.farrukh.communityapp.di.car.Engine
import khusainov.farrukh.communityapp.di.car.SpecificEngineImpl

/**
 *Created by farrukh_kh on 10/18/21 11:00 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class SpecificEngineImplModule{

	@Binds
	abstract fun provideEngine(specificEngineImpl: SpecificEngineImpl): Engine
}