package khusainov.farrukh.communityapp.di.car

import android.util.Log
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/18/21 10:20 PM
 *khusainov.farrukh.communityapp.di
 **/
class EngineImpl @Inject constructor() : Engine {

	private val TAG = "EngineImpl"

	init {
		Log.e(TAG, "engine: created")
	}
}