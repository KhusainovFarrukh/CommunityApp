package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
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
    private val _responseComments = MutableLiveData<MutableList<ArticleDetails>>()
    private val _isLiked = MutableLiveData(false)

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val isLoadingComments: LiveData<Boolean> = _isLoadingComments
    val responseArticle: LiveData<Response<ArticleDetails>> = _responseArticle
    val responseComments: MutableLiveData<MutableList<ArticleDetails>> = _responseComments
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

            _responseComments.postValue(repository.getComments(idList) as MutableList<ArticleDetails>?)

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
            try {
                if (_isLiked.value == true) {
                    repository.removeLikeArticleById(articleId)
                    _isLiked.postValue(false)
                } else {
                    repository.likeArticleById(articleId)
                    _isLiked.postValue(true)
                }
            } catch (e: Exception) {
                Log.wtf("error", e.message)
            }
        }
    }

    fun likeCommentById(commentId: String, isLiked: Boolean) {
        viewModelScope.launch {
            _responseComments.value?.forEach {
                if (it.articleId == commentId) {
                    if (isLiked) {
                        _responseComments.value!![_responseComments.value!!.indexOf(it)] =
                            repository.removeLikeArticleById(commentId)
                    } else {
                        _responseComments.value!![_responseComments.value!!.indexOf(it)] =
                            repository.likeArticleById(commentId)
                    }
                }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }
}