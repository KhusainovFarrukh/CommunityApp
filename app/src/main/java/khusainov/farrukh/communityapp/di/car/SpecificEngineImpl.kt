package khusainov.farrukh.communityapp.di.car

import android.util.Log
import khusainov.farrukh.communityapp.di.dagger.FuelConsumePerKm
import khusainov.farrukh.communityapp.di.dagger.HorsePower
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/18/21 10:59 PM
 *khusainov.farrukh.communityapp.di
 **/
class SpecificEngineImpl @Inject constructor(
	@HorsePower private val horsePower: Int,
	@FuelConsumePerKm private val fuelConsumePerKm: Int,
) : Engine {

	private val TAG = "SpecificEngineImpl"

	init {
		Log.e(TAG,
			"engine: created (horse power = $horsePower; fuel consume per km = $fuelConsumePerKm)")
	}
}