package khusainov.farrukh.communityapp.ui.notifications.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import khusainov.farrukh.communityapp.data.notifications.NotificationsRepository
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/4/21 10:52 PM
 **/
@HiltViewModel
class NotificationsViewModel @Inject constructor(
	repository: NotificationsRepository,
) : ViewModel() {

	//notifications value
	val notificationsLiveData = repository.getNotifications().cachedIn(viewModelScope)
}