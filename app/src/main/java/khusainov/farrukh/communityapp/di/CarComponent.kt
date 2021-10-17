package khusainov.farrukh.communityapp.di

import dagger.Component
import khusainov.farrukh.communityapp.ui.activities.HomeActivity

/**
 *Created by farrukh_kh on 10/7/21 11:55 PM
 *khusainov.farrukh.communityapp.di
 **/
@Component
interface CarComponent {

	fun getCar(): Car

	fun inject(activity: HomeActivity)
}