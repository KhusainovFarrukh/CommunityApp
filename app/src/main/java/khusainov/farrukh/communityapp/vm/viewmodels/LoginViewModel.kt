package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.SignInData
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class LoginViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var loginJob: Job
    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseUser = MutableLiveData<User>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseUser: LiveData<User> = _responseUser

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun signInWithEmail(signInData: SignInData) {
        loginJob = coroutineScope.launch {
            _isLoading.postValue(true)

            repository.signIn(signInData).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseUser.postValue(dataWrapper.data)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun cancelJob() {
        loginJob.cancel()
        _isLoading.postValue(false)
    }
}