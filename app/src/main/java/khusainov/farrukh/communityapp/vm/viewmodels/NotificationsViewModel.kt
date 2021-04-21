package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.repository.Repository

/**
 *Created by FarrukhKhusainov on 3/4/21 10:52 PM
 **/
class NotificationsViewModel(repository: Repository) : ViewModel() {

//    private val _isLoading = MutableLiveData<Boolean>()
//    private val _responseNotifications = MutableLiveData<Response<List<Notification>>>()

//    val isLoading: LiveData<Boolean> = _isLoading

    val responseNotification = repository.getNotifications().cachedIn(viewModelScope)

//    init {
//        coroutineScope.launch {
//            _isLoading.postValue(true)
//
//            _responseNotifications.postValue(repository.getNotifications())
//
//            _isLoading.postValue(false)
//        }
//    }
}