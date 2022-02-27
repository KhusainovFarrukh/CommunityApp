package khusainov.farrukh.communityapp.ui.auth.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.data.auth.AuthRepository
import khusainov.farrukh.communityapp.data.auth.remote.SignInRequest
import khusainov.farrukh.communityapp.data.utils.models.OtherError
import khusainov.farrukh.communityapp.data.user.remote.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
@HiltViewModel
class LoginViewModel @Inject constructor(
	private val repository: AuthRepository,
) : ViewModel() {

	/**
	[_isLoading] - sign in loading state
	[_userLiveData] - signed in user value
	[_signInError] - error while signing in
	 */

	//private mutable live data:
	private val _isLoading = MutableLiveData<Boolean>()
	private val _userLiveData = MutableLiveData<User>()
	private val _signInError = MutableLiveData<OtherError>()

	//public immutable live data:
	val isLoading: LiveData<Boolean> get() = _isLoading
	val userLiveData: LiveData<User> get() = _userLiveData
	val signInError: LiveData<OtherError> get() = _signInError

	//fun to sign in user
	fun signInWithEmail(signInRequest: SignInRequest) {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.signIn(signInRequest).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _userLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _signInError.postValue(OtherError(dataWrapper.message)
					{ signInWithEmail(signInRequest) })
				}
			}

			_isLoading.postValue(false)
		}
	}
}