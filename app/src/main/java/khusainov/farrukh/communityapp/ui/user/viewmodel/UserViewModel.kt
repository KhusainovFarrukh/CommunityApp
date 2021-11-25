package khusainov.farrukh.communityapp.ui.user.viewmodel

import androidx.lifecycle.*
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.OtherError
import khusainov.farrukh.communityapp.data.user.UserRepository
import khusainov.farrukh.communityapp.data.user.remote.User
import khusainov.farrukh.communityapp.di.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/5/21 2:58 PM
 **/
class UserViewModel @Inject constructor(
	@UserId private val userId: String,
	private val repository: UserRepository,
) : ViewModel() {

	/**
	[_isLoading] - user loading state
	[_userLiveData] - user value
	[_errorUser] - error while initializing user
	[_otherError] - error while executing some functions (follow)
	 */

	//private mutable live data:
	private val _isLoading = MutableLiveData<Boolean>()
	private val _userLiveData = MutableLiveData<User>()
	private val _errorUser = MutableLiveData<String>()
	private val _otherError = MutableLiveData<OtherError>()

	//public immutable live data:
	val isLoading: LiveData<Boolean> = _isLoading
	val userLiveData: LiveData<User> = _userLiveData
	val errorUser: LiveData<String> get() = _errorUser
	val otherError: LiveData<OtherError> get() = _otherError


	init {
		initUser()
	}

	//fun to initialize user
	fun initUser() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.getUser(userId).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _userLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _errorUser.postValue(dataWrapper.message)
				}
			}

			_isLoading.postValue(false)
		}
	}

	//fun to follow/unfollow user
	fun followUser() {
		viewModelScope.launch(Dispatchers.IO) {
			repository.followUser(_userLiveData.value!!).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _userLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message) { followUser() })
				}
			}
		}
	}
}