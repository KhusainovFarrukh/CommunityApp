package khusainov.farrukh.communityapp

import android.app.Application
import androidx.fragment.app.Fragment
import khusainov.farrukh.communityapp.di.AppComponent
import khusainov.farrukh.communityapp.di.DaggerAppComponent

/**
 *Created by farrukh_kh on 10/29/21 10:02 AM
 *khusainov.farrukh.communityapp
 **/
class CommunityApp : Application() {

	lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()

		appComponent = DaggerAppComponent.factory().create(this)
	}
}

fun Fragment.getAppComponent() = (requireActivity().application as CommunityApp).appComponent