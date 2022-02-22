package khusainov.farrukh.communityapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.*
import javax.inject.*
import kotlin.reflect.KClass

/**
 *Created by farrukh_kh on 2/20/22 12:58 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class ViewModelFactoryModule {

	@Binds
	abstract fun bindViewModelFactory(communityAppViewModelFactory: CommunityAppViewModelFactory): ViewModelProvider.Factory
}

class CommunityAppViewModelFactory @Inject constructor(
	private val providers: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
) : ViewModelProvider.Factory {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		var provider: Provider<ViewModel>? = providers[modelClass]

		if (provider == null) {
			for ((key, value) in providers) {
				if (modelClass.isAssignableFrom(key)) {
					provider = value
					break
				}
			}
		}

		if (provider == null) {
			throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
		}

		@Suppress("UNCHECKED_CAST")
		try {
			return provider.get() as T
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	}

}

@Target(AnnotationTarget.FUNCTION,
	AnnotationTarget.PROPERTY_GETTER,
	AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)