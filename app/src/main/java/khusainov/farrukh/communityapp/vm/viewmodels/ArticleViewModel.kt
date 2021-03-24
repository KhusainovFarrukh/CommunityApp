package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.models.User
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleViewModel(private val articleId: String, private val repository: Repository) :
    ViewModel() {

    private val _isLoadingArticle = MutableLiveData<Boolean>()
    private val _isLoadingComments = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<Post>>()
    private val _responseComments = MutableLiveData<Response<List<Post>>>()
    private val _isLiked = MutableLiveData(false)

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val isLoadingComments: LiveData<Boolean> = _isLoadingComments
    val responseArticle: LiveData<Response<Post>> = _responseArticle
    val responseComments: MutableLiveData<Response<List<Post>>> = _responseComments
    val isLiked: LiveData<Boolean> = _isLiked

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoadingArticle.postValue(true)

            _responseArticle.postValue(repository.getArticle(articleId))

            _isLoadingArticle.postValue(false)
        }
        coroutineScope.launch {
            _isLoadingComments.postValue(true)

            _responseComments.postValue(repository.getComments(articleId))

            _isLoadingComments.postValue(false)
        }
    }

    fun isLiked(id: String, idList: List<User>) {
        idList.forEach {
            if (it.id == id) {
                _isLiked.postValue(true)
                return@forEach
            }
        }
    }

    fun likeArticle(articleId: String) {
        coroutineScope.launch {
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
        coroutineScope.launch {
            (_responseComments.value?.body() as MutableList<Post>).let {
                it.forEach { currentItem ->
                    if (currentItem.id == commentId) {
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
        coroutineScope.launch {
            if (isLiked) {
                repository.removeLikeArticle(commentId)
            } else {
                repository.likeArticle(commentId)
            }

            _responseComments.postValue(repository.getComments(articleId))
        }
    }

    fun addCommentToArticle(body: String) {
        coroutineScope.launch {
            (_responseComments.value?.body() as MutableList<Post>).let { list ->
                repository.addComment(body, _responseArticle.value!!.body()!!).body()
                    ?.let {
                        list.add(it)
                    }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun addCommentToComment(body: String, parentComment: Post) {
        coroutineScope.launch {
            repository.addCommentToComment(body, parentComment)
            _responseComments.postValue(repository.getComments(articleId))
        }
    }

    fun deleteComment(commentId: String) {
        coroutineScope.launch {
            repository.deleteArticle(commentId)
            (_responseComments.value?.body() as MutableList<Post>).let {
                it.forEach { currentItem ->
                    if (currentItem.id == commentId) {
                        it.remove(currentItem)
                        return@forEach
                    }
                }
            }
            _responseComments.postValue(_responseComments.value)
        }
    }

    fun deleteSubComment(commentId: String) {
        coroutineScope.launch {
            repository.deleteArticle(commentId)
            _responseComments.postValue(repository.getComments(articleId))
        }
    }
}