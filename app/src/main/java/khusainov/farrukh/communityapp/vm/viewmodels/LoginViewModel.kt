package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.SignInData
import khusainov.farrukh.communityapp.data.model.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class LoginViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var loginJob: Job
    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseUser = MutableLiveData<Response<User>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseUser: LiveData<Response<User>> = _responseUser

    fun signIn(signInData: SignInData) {
        loginJob = viewModelScope.launch {
            _isLoading.postValue(true)

            _responseUser.postValue(repository.signIn(signInData))

            _isLoading.postValue(false)
        }
    }

    fun cancelJob() {
        loginJob.cancel()
        _isLoading.postValue(false)
    }
}