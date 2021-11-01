package khusainov.farrukh.communityapp.ui.notifications.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.notifications.NotificationsRepository

/**
 *Created by FarrukhKhusainov on 3/4/21 10:52 PM
 **/
class NotificationsViewModel(repository: NotificationsRepository) : ViewModel() {

	//notifications value
	val notificationsLiveData = repository.getNotifications().cachedIn(viewModelScope)
}

class NotificationsViewModelFactory(
	private val repository: NotificationsRepository,
) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
			return NotificationsViewModel(repository) as T
		}
		throw IllegalArgumentException("$modelClass is not NotificationsViewModel")
	}
}