package khusainov.farrukh.communityapp

import android.app.Activity
import android.app.Application
import khusainov.farrukh.communityapp.di.dagger.*

/**
 *Created by farrukh_kh on 10/29/21 10:02 AM
 *khusainov.farrukh.communityapp
 **/
class CommunityApp : Application() {

	lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()

		appComponent = DaggerAppComponent.create()
	}
}

fun Activity.getAppComponent() = (application as CommunityApp).appComponent