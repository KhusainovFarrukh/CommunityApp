package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.data.model.ArticleDetailsWithResponses
import khusainov.farrukh.communityapp.data.model.UserModel
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleDetailsViewModel(private val articleId: String, private val repository: Repository) :
    ViewModel() {

    private val _isLoadingArticle = MutableLiveData<Boolean>()
    private val _isLoadingComments = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<ArticleDetails>>()
    private val _responseComments = MutableLiveData<Response<List<ArticleDetailsWithResponses>>>()
    private val _isLiked = MutableLiveData(false)

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val isLoadingComments: LiveData<Boolean> = _isLoadingComments
    val responseArticle: LiveData<Response<ArticleDetails>> = _responseArticle
    val responseComments: MutableLiveData<Response<List<ArticleDetailsWithResponses>>> =
        _responseComments
    val isLiked: LiveData<Boolean> = _isLiked

    init {
        viewModelScope.launch {
            _isLoadingArticle.postValue(true)

            _responseArticle.postValue(repository.getArticleById(articleId))

            _isLoadingArticle.postValue(false)
        }
        viewModelScope.launch {
            _isLoadingComments.postValue(true)

            _responseComments.postValue(repository.getComments(articleId))

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
            (_responseComments.value?.body() as MutableList<ArticleDetailsWithResponses>).let {
                it.forEach { currentItem ->
                    if (currentItem.articleId == commentId) {
                        if (isLiked) {
                            it[it.indexOf(currentItem)] =
                                repository.removeLikeArticleById(commentId)
                        } else {
                            it[it.indexOf(currentItem)] = repository.likeArticleById(commentId)
                        }
                        return@forEach
                    }
                }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun addCommentToArticle(comment: String) {
        viewModelScope.launch {
            (_responseComments.value?.body() as MutableList<ArticleDetailsWithResponses>).let { list ->
                repository.addCommentToArticle(comment, _responseArticle.value!!.body()!!).body()
                    ?.let {
                        list.add(it)
                    }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }
}