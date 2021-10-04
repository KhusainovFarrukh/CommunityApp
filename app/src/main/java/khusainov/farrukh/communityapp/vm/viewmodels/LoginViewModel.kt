package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.models.OtherError
import khusainov.farrukh.communityapp.data.models.SignInData
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class LoginViewModel(private val repository: Repository) : ViewModel() {

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
	fun signInWithEmail(signInData: SignInData) {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.signIn(signInData).let { dataWrapper ->
				when (dataWrapper) {
                    is DataWrapper.Success -> _userLiveData.postValue(dataWrapper.data)
                    is DataWrapper.Error -> _signInError.postValue(OtherError(dataWrapper.message)
                    { signInWithEmail(signInData) })
				}
			}

			_isLoading.postValue(false)
		}
	}
}