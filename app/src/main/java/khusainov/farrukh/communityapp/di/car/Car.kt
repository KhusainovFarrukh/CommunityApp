package khusainov.farrukh.communityapp.di.car

import android.util.Log
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/7/21 11:46 PM
 *khusainov.farrukh.communityapp.di
 **/
class Car @Inject constructor(
	private val engine: Engine,
	private val wheel: Wheel,
) {

	private val TAG = "Car"

	@Inject
	lateinit var fifthWheel: Wheel

	@Inject
	fun installFifthWheel() {
		fifthWheel.setCar(this)
	}

	fun drive() {
		Log.e(TAG, "drive: driving...")
	}
}