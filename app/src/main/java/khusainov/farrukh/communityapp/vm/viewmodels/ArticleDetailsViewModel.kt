package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.data.model.UserModel
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleDetailsViewModel(articleId: String, private val repository: Repository) : ViewModel() {

    private val _isLoadingArticle = MutableLiveData<Boolean>()
    private val _isLoadingComments = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<ArticleDetails>>()
    private val _responseComments = MutableLiveData<List<ArticleDetails>>()
    private val _isLiked = MutableLiveData(false)

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val isLoadingComments: LiveData<Boolean> = _isLoadingComments
    val responseArticle: LiveData<Response<ArticleDetails>> = _responseArticle
    val responseComments: LiveData<List<ArticleDetails>> = _responseComments
    val isLiked: LiveData<Boolean> = _isLiked

    init {
        viewModelScope.launch {
            _isLoadingArticle.postValue(true)

            _responseArticle.postValue(repository.getArticleById(articleId))

            _isLoadingArticle.postValue(false)
        }
    }

    fun getComments(idList: List<String>) {
        viewModelScope.launch {
            _isLoadingComments.postValue(true)

            _responseComments.postValue(repository.getComments(idList))

            _isLoadingComments.postValue(false)
        }
    }

    fun isLiked(id: String, idList: List<UserModel>) {
        idList.forEach {
            if (it.userId == id) {
                _isLiked.postValue(true)
                return@forEach
            }
        }
    }

    fun likeArticleById(articleId: String) {
        viewModelScope.launch {
            if (_isLiked.value == true) {
                _isLiked.postValue(repository.removeLikeArticleById(articleId))
            } else {
                _isLiked.postValue(repository.likeArticleById(articleId))
            }
        }
    }
}