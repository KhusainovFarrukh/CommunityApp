package khusainov.farrukh.communityapp

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import khusainov.farrukh.communityapp.di.components.AppComponent
import khusainov.farrukh.communityapp.di.components.DaggerAppComponent
import khusainov.farrukh.communityapp.di.dagger_mock.*

/**
 *Created by farrukh_kh on 10/29/21 10:02 AM
 *khusainov.farrukh.communityapp
 **/
class CommunityApp : Application() {

	lateinit var appComponentMock: AppComponentMock
	lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()

		appComponentMock = DaggerAppComponentMock.create()
		appComponent = DaggerAppComponent.factory().create(this)
	}
}

fun Activity.getAppComponentMock() = (application as CommunityApp).appComponentMock

fun Activity.getAppComponent() = (application as CommunityApp).appComponent

fun Fragment.getAppComponent() = requireActivity().getAppComponent()