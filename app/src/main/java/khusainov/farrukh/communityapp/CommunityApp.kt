package khusainov.farrukh.communityapp

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import khusainov.farrukh.communityapp.di.DaggerAppComponent

/**
 *Created by farrukh_kh on 10/29/21 10:02 AM
 *khusainov.farrukh.communityapp
 **/
class CommunityApp : DaggerApplication() {
	override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
		return DaggerAppComponent.factory().create(this)

	}
}