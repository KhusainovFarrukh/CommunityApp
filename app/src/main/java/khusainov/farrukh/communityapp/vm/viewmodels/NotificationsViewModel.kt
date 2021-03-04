package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.model.Notification
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:52 PM
 **/
class NotificationsViewModel(cookies: List<String>, repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseNotifications = MutableLiveData<Response<List<Notification>>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseNotification: LiveData<Response<List<Notification>>> = _responseNotifications

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseNotifications.postValue(repository.getNotifications(cookies[0], cookies[1]))

            _isLoading.postValue(false)
        }
    }
}