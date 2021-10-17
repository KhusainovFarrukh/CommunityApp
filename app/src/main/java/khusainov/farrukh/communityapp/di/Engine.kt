package khusainov.farrukh.communityapp.di

import android.util.Log
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/7/21 11:47 PM
 *khusainov.farrukh.communityapp.di
 **/
class Engine @Inject constructor() {

	private val TAG = "Engine"

	init {
		Log.e(TAG, "engine: created")
	}
}