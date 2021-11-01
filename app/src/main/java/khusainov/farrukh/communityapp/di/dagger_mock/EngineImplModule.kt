package khusainov.farrukh.communityapp.di.dagger_mock

import dagger.*
import khusainov.farrukh.communityapp.di.car.Engine
import khusainov.farrukh.communityapp.di.car.EngineImpl

/**
 *Created by farrukh_kh on 10/18/21 10:29 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
class EngineImplModule(private val horsePower: Int) {

	@Provides
	fun provideHorsePower() = horsePower

	@Provides
	fun provideEngine(engineImpl: EngineImpl) = engineImpl as Engine
}