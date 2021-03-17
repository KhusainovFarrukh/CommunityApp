package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.data.model.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/5/21 2:58 PM
 **/

//TODO add userId to constructor
class UserViewModel(private val userId: String, private val repository: Repository) : ViewModel() {

    private val _responseUser = MutableLiveData<Response<User>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _usersArticles = MutableLiveData<Response<List<Article>>>()
    private val _usersComments = MutableLiveData<Response<List<Article>>>()
    private val _isFollowed = MutableLiveData<Boolean>()

    val responseUser: LiveData<Response<User>> = _responseUser
    val isLoading: LiveData<Boolean> = _isLoading
    val usersArticles: LiveData<Response<List<Article>>> = _usersArticles
    val usersComments: LiveData<Response<List<Article>>> = _usersComments
    val isFollowed: LiveData<Boolean> = _isFollowed

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseUser.postValue(repository.getUserById(userId))

            _isLoading.postValue(false)

            _responseUser.value?.body()?.let {
                _isFollowed.postValue(it.followed)
            }

            _usersArticles.postValue(repository.getPostsOfUserById(userId, "article"))
//            _usersComments.postValue(repository.getPostsOfUserById(userId, "response"))
        }
    }

    //TODO edit this to return User data class
    fun followUserById() {
        viewModelScope.launch {
            if (_isFollowed.value == true) {
                repository.unFollowUserById(userId)
                _isFollowed.postValue(false)
            } else {
                repository.followUserById(userId)
                _isFollowed.postValue(true)
            }
        }
    }
}