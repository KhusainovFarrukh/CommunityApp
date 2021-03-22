package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
class PostsOfUserViewModel(
    private val userId: String,
    private val type: String,
    private val sortBy: String,
    private val repository: Repository,
) : ViewModel() {

    private val _usersPosts = MutableLiveData<Response<List<Article>>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val usersPosts: LiveData<Response<List<Article>>> = _usersPosts
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _usersPosts.postValue(repository.getPostsOfUserById(userId, type, sortBy))

            _isLoading.postValue(false)
        }
    }
}