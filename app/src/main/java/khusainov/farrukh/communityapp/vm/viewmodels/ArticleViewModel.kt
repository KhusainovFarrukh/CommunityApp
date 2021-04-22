package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.DataWrapper
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

    private val _isLiked = MutableLiveData(false)

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle

    val isLoadingComments: LiveData<Boolean> = _isLoadingComments
    val responseArticle: LiveData<Response<Post>> = _responseArticle

    val isLiked: LiveData<Boolean> = _isLiked

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var _comments = repository.getComments(articleId)
        .cachedIn(viewModelScope)
        .let {
            it as MutableLiveData<PagingData<Post>>
        }

    val comments: LiveData<PagingData<Post>> get() = _comments

    init {
        coroutineScope.launch {
            _isLoadingArticle.postValue(true)

            _responseArticle.postValue(repository.getArticle(articleId))

            _isLoadingArticle.postValue(false)
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

    suspend fun likeCommentTemp(comment: Post, refresh: () -> Unit) {
        if (comment.isLiked) {
            repository.removeLikeArticle(comment.id).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
        } else {
            repository.likeArticle(comment.id).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
        }
    }

    suspend fun deleteCommentTemp(commentId: String, refresh: () -> Unit) {
        //TODO add wrapper
        repository.deleteArticle(commentId)
        refresh.invoke()
    }

    suspend fun replyCommentTemp(replyBody: String, replyTo: String, refresh: () -> Unit) {
        repository.addCommentToComment(replyBody, _responseArticle.value!!.body()!!, replyTo).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
    }

    suspend fun addCommentWithPaging(commentBody: String, refresh: () -> Unit) {
        repository.addComment(commentBody, _responseArticle.value!!.body()!!).let { wrapper ->
            when (wrapper) {
                is DataWrapper.Success -> refresh.invoke()
                is DataWrapper.Error -> Log.e("TAG", wrapper.message)
            }
        }
    }
}