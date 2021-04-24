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
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleViewModel(private val articleId: String, private val repository: Repository) :
    ViewModel() {

    private val _isLoadingArticle = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Post>()
    private val _errorArticle = MutableLiveData<String>()

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val responseArticle: LiveData<Post> = _responseArticle
    val errorArticle: LiveData<String> get() = _errorArticle

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var _comments = repository.getComments(articleId)
        .cachedIn(viewModelScope)
        .let {
            it as MutableLiveData<PagingData<Post>>
        }

    val comments: LiveData<PagingData<Post>> get() = _comments

    init {
        initializeArticle()
    }

    fun initializeArticle() {
        coroutineScope.launch {
            _isLoadingArticle.postValue(true)

            repository.getArticle(articleId).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseArticle.postValue(dataWrapper.data)
                    is DataWrapper.Error -> _errorArticle.postValue(dataWrapper.message)
                }
            }

            _isLoadingArticle.postValue(false)
        }
    }

    fun likeArticle(articleId: String) {
        viewModelScope.launch {
            if (_responseArticle.value!!.isLiked) {
                repository.removeLikePost(articleId).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> _responseArticle.postValue(dataWrapper.data)
                        is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                    }
                }
            } else {
                repository.likePost(articleId).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> _responseArticle.postValue(dataWrapper.data)
                        is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                    }
                }
            }
        }
    }

    suspend fun likeCommentTemp(comment: Post, refresh: () -> Unit) {
        if (comment.isLiked) {
            repository.removeLikePost(comment.id).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
        } else {
            repository.likePost(comment.id).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
        }
    }

    suspend fun deleteCommentTemp(commentId: String, refresh: () -> Unit) {
        repository.deletePost(commentId).let { dataWrapper ->
            when (dataWrapper) {
                is DataWrapper.Success -> refresh.invoke()
                is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
            }
        }
    }

    suspend fun replyCommentTemp(replyBody: String, replyTo: String, refresh: () -> Unit) {
        repository.replyToComment(replyBody, _responseArticle.value!!, replyTo)
            .let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> Log.wtf("VM", dataWrapper.message)
                }
            }
    }

    suspend fun addCommentWithPaging(commentBody: String, refresh: () -> Unit) {
        repository.addComment(commentBody, _responseArticle.value!!).let { wrapper ->
            when (wrapper) {
                is DataWrapper.Success -> refresh.invoke()
                is DataWrapper.Error -> Log.e("TAG", wrapper.message)
            }
        }
    }
}