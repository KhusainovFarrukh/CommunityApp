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

            _responseArticle.postValue(repository.getArticle(articleId))

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

    fun likeArticle(articleId: String) {
        viewModelScope.launch {
            try {
                if (_isLiked.value == true) {
                    repository.removeLikeArticle(articleId)
                    _isLiked.postValue(false)
                } else {
                    repository.likeArticle(articleId)
                    _isLiked.postValue(true)
                }
            } catch (e: Exception) {
                Log.wtf("error", e.message)
            }
        }
    }

    fun likeComment(commentId: String, isLiked: Boolean) {
        viewModelScope.launch {
            (_responseComments.value?.body() as MutableList<ArticleDetailsWithResponses>).let {
                it.forEach { currentItem ->
                    if (currentItem.articleId == commentId) {
                        if (isLiked) {
                            repository.removeLikeArticle(commentId).let { likedComment ->
                                it[it.indexOf(currentItem)] = currentItem.copy(
                                    stats = likedComment.stats,
                                    isLiked = likedComment.isLiked
                                )
                            }
                        } else {
                            repository.likeArticle(commentId).let { likedComment ->
                                it[it.indexOf(currentItem)] = currentItem.copy(
                                    stats = likedComment.stats,
                                    isLiked = likedComment.isLiked
                                )
                            }
                        }
                        return@forEach
                    }
                }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun likeSubComment(commentId: String, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                repository.removeLikeArticle(commentId)
            } else {
                repository.likeArticle(commentId)
            }

            _responseComments.postValue(repository.getComments(articleId))
        }
    }

    fun addCommentToArticle(body: String) {
        viewModelScope.launch {
            (_responseComments.value?.body() as MutableList<ArticleDetailsWithResponses>).let { list ->
                repository.addComment(body, _responseArticle.value!!.body()!!).body()
                    ?.let {
                        list.add(it)
                    }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun addCommentToComment(body: String, parentComment: ArticleDetailsWithResponses) {
        viewModelScope.launch {
            repository.addCommentToComment(body, parentComment)
            _responseComments.postValue(repository.getComments(articleId))
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            repository.deleteArticle(commentId)
            (_responseComments.value?.body() as MutableList<ArticleDetailsWithResponses>).let {
                it.forEach { currentItem ->
                    if (currentItem.articleId == commentId) {
                        it.remove(currentItem)
                        return@forEach
                    }
                }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun deleteSubComment(commentId: String) {
        viewModelScope.launch {
            repository.deleteArticle(commentId)
            _responseComments.postValue(repository.getComments(articleId))
        }
    }
}