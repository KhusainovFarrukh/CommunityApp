package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/5/21 2:58 PM
 **/
class UserViewModel(private val userId: String, private val repository: Repository) : ViewModel() {

    private val _responseUser = MutableLiveData<User>()
    private val _isLoading = MutableLiveData<Boolean>()

    val responseUser: LiveData<User> = _responseUser
    val isLoading: LiveData<Boolean> = _isLoading

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        initializeUser()
    }

    fun initializeUser() {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.getUser(userId).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun followUser() {
        coroutineScope.launch {
            if (_responseUser.value!!.followed) {
                repository.unFollowUser(userId).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                        is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                    }
                }
            } else {
                repository.followUser(userId).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                        is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                    }
                }
            }
        }
    }
}