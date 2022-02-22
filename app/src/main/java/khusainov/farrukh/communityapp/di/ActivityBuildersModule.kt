package khusainov.farrukh.communityapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import khusainov.farrukh.communityapp.ui.activities.HomeActivity

/**
 *Created by farrukh_kh on 2/19/22 4:28 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class ActivityBuildersModule {

	@ActivityScope
	@ContributesAndroidInjector(modules = [HomeActivityModule::class])
	abstract fun contributeHomeActivity(): HomeActivity
}