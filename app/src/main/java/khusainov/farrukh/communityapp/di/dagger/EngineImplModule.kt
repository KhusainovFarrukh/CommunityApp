package khusainov.farrukh.communityapp.di.dagger

import dagger.*
import khusainov.farrukh.communityapp.di.car.Engine
import khusainov.farrukh.communityapp.di.car.EngineImpl

/**
 *Created by farrukh_kh on 10/18/21 10:29 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class EngineImplModule {

	@Binds
	abstract fun provideEngine(engineImpl: EngineImpl): Engine
}