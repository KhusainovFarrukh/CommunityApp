package khusainov.farrukh.communityapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.model.Notification
import khusainov.farrukh.communityapp.model.SignInData
import khusainov.farrukh.communityapp.model.User
import khusainov.farrukh.communityapp.repo.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {

    private lateinit var loginJob: Job
    private val repo = Repository()
    private val _isLoadingLogin = MutableLiveData<Boolean>()
    private val _isLoadingArticles = MutableLiveData<Boolean>()
    private val _isLoadingNotifications = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<Article>>()
    private val _responseUser = MutableLiveData<Response<User>>()
    private val _responseNotif = MutableLiveData<Response<List<Notification>>>()
    private val _responseAllPosts = MutableLiveData<Response<List<Article>>>()

    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin
    val isLoadingArticles: LiveData<Boolean> = _isLoadingArticles
    val isLoadingNotifications: LiveData<Boolean> = _isLoadingNotifications
    val responseArticle: LiveData<Response<Article>> = _responseArticle
    val responseUser: LiveData<Response<User>> = _responseUser
    val responseNotification: LiveData<Response<List<Notification>>> = _responseNotif
    val responseAllPosts: LiveData<Response<List<Article>>> = _responseAllPosts

    fun getArticle(articleId: String) {
        viewModelScope.launch {
            _isLoadingLogin.postValue(true)

            _responseArticle.postValue(repo.getArticle(articleId))

            _isLoadingLogin.postValue(false)
        }
    }

    fun signIn(signInData: SignInData) {
        loginJob = viewModelScope.launch {
            _isLoadingLogin.postValue(true)

            _responseUser.postValue(repo.signIn(signInData))

            _isLoadingLogin.postValue(false)
        }
    }

    fun getNotifications(cookie1: String, cookie2: String) {
        viewModelScope.launch {
            _isLoadingNotifications.postValue(true)

            _responseNotif.postValue(repo.getNotifications(cookie1, cookie2))

            _isLoadingNotifications.postValue(false)
        }
    }

    fun getAllPosts(limit: Int, type: String) {
        viewModelScope.launch {
            _isLoadingArticles.postValue(true)

            _responseAllPosts.postValue(repo.getAllPosts(limit, type))

            _isLoadingArticles.postValue(false)
        }
    }

    fun cancelJob() {
        loginJob.cancel()
        _isLoadingLogin.postValue(false)
    }
}