package khusainov.farrukh.communityapp

import android.app.Application

/**
 *Created by farrukh_kh on 10/29/21 10:02 AM
 *khusainov.farrukh.communityapp
 **/
class CommunityApp : Application() {

	override fun onCreate() {
		super.onCreate()

		appComponent = DaggerAppComponent.create()
	}
}