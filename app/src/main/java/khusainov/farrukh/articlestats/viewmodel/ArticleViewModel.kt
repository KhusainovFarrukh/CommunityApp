package khusainov.farrukh.articlestats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.model.Notif
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import khusainov.farrukh.articlestats.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel : ViewModel() {

    private val repo = Repository()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<Article>>()
    private val _responseUser = MutableLiveData<Response<User>>()
    private val _responseNotif = MutableLiveData<Response<List<Notif>>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseArticle: LiveData<Response<Article>> = _responseArticle
    val responseUser: LiveData<Response<User>> = _responseUser
    val responseNotif: LiveData<Response<List<Notif>>> = _responseNotif

    fun getArticle(articleId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseArticle.postValue(repo.getArticle(articleId))

            _isLoading.postValue(false)
        }
    }

    fun signIn(signInData: SignInData) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseUser.postValue(repo.signIn(signInData))

            _isLoading.postValue(false)
        }
    }

    fun getNotifications(cookie1: String, cookie2: String) {
        viewModelScope.launch {
            _responseNotif.postValue(repo.getNotifications(cookie1, cookie2))
        }
    }
}