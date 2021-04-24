package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.OtherError
import khusainov.farrukh.communityapp.data.models.SignInData
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseUser = MutableLiveData<User>()
    private val _otherError = MutableLiveData<OtherError>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseUser: LiveData<User> = _responseUser
    val otherError: LiveData<OtherError> get() = _otherError

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun signInWithEmail(signInData: SignInData) {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.signIn(signInData).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                    is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                    ) { signInWithEmail(signInData) })
                }
            }

            _isLoading.postValue(false)
        }
    }
}