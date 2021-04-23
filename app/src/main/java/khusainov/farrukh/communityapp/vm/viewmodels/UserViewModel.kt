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
    private val _isFollowed = MutableLiveData<Boolean>()

    val responseUser: LiveData<User> = _responseUser
    val isLoading: LiveData<Boolean> = _isLoading
    val isFollowed: LiveData<Boolean> = _isFollowed

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.getUser(userId).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoading.postValue(false)

            _responseUser.value?.let {
                _isFollowed.postValue(it.followed)
            }
        }
    }

    //TODO edit this to return User data class
    fun followUser() {
        coroutineScope.launch {
            if (_isFollowed.value == true) {
                repository.unFollowUser(userId)
                _isFollowed.postValue(false)
            } else {
                repository.followUser(userId)
                _isFollowed.postValue(true)
            }
        }
    }
}