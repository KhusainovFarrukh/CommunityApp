package khusainov.farrukh.communityapp.di.car

import android.util.Log

/**
 *Created by farrukh_kh on 10/7/21 11:47 PM
 *khusainov.farrukh.communityapp.di
 **/
class Wheel(
	tire: Tire,
	rim: Rim
) {

	private val TAG = "Wheel"

	init {
		Log.e(TAG, "wheel: created")
	}

	fun setCar(car: Car) {
		Log.e(TAG, "setCar: fifthWheel is installed")
	}
}