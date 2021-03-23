package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/5/21 2:58 PM
 **/
class UserViewModel(private val userId: String, private val repository: Repository) : ViewModel() {

    private val _responseUser = MutableLiveData<Response<User>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isFollowed = MutableLiveData<Boolean>()

    val responseUser: LiveData<Response<User>> = _responseUser
    val isLoading: LiveData<Boolean> = _isLoading
    val isFollowed: LiveData<Boolean> = _isFollowed

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseUser.postValue(repository.getUser(userId))

            _isLoading.postValue(false)

            _responseUser.value?.body()?.let {
                _isFollowed.postValue(it.followed)
            }
        }
    }

    //TODO edit this to return User data class
    fun followUser() {
        viewModelScope.launch {
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