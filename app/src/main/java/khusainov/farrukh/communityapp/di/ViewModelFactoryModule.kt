package khusainov.farrukh.communityapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 *Created by farrukh_kh on 11/24/21 8:57 PM
 *khusainov.farrukh.communityapp.di
 **/
@Module
abstract class ViewModelFactoryModule {

	@Binds
	abstract fun bindViewModelFactory(
		communityViewModelFactory: CommunityViewModelFactory,
	): ViewModelProvider.Factory
}

class CommunityViewModelFactory @Inject constructor(
	private val providers: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
) : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		var provider: Provider<out ViewModel>? = providers[modelClass]
		if (provider == null) {
			for ((key, value) in providers) {
				if (modelClass.isAssignableFrom(key)) {
					provider = value
					break
				}
			}
		}
		if (provider == null) {
			throw IllegalArgumentException("Unknown model class: $modelClass")
		}
		try {
			@Suppress("UNCHECKED_CAST")
			return provider.get() as T
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	}
}

@Target(
	AnnotationTarget.FUNCTION,
	AnnotationTarget.PROPERTY_GETTER,
	AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)