package khusainov.farrukh.communityapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import khusainov.farrukh.communityapp.utils.Constants
import javax.inject.Singleton

/**
 *Created by farrukh_kh on 11/1/21 12:55 PM
 *khusainov.farrukh.communityapp.di.modules
 **/
@Module
abstract class SharedPrefsModule {
	companion object {
		@Singleton
		@Provides
		fun provideSharedPrefs(context: Context): SharedPreferences =
			context.getSharedPreferences(Constants.KEY_COOKIES_REQUEST,
				Context.MODE_PRIVATE)

		@Singleton
		@Provides
		fun provideSharedPrefsEditor(sharedPrefs: SharedPreferences): SharedPreferences.Editor =
			sharedPrefs.edit()
	}
}