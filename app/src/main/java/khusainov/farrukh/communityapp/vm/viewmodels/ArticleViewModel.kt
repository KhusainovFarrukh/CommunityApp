package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.OtherError
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
    private val _otherError = MutableLiveData<OtherError>()

    val isLoadingArticle: LiveData<Boolean> = _isLoadingArticle
    val responseArticle: LiveData<Post> = _responseArticle
    val errorArticle: LiveData<String> get() = _errorArticle
    val otherError: LiveData<OtherError> get() = _otherError

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
                        is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                        ) { likeArticle(articleId) })
                    }
                }
            } else {
                repository.likePost(articleId).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> _responseArticle.postValue(dataWrapper.data)
                        is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                        ) { likeArticle(articleId) })
                    }
                }
            }
        }
    }

    fun likeCommentTemp(comment: Post, refresh: () -> Unit) {
        viewModelScope.launch {
            if (comment.isLiked) {
                repository.removeLikePost(comment.id).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> refresh.invoke()
                        is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                        ) { likeCommentTemp(comment, refresh) })
                    }
                }
            } else {
                repository.likePost(comment.id).let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> refresh.invoke()
                        is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                        ) { likeCommentTemp(comment, refresh) })
                    }
                }
            }
        }
    }

    fun deleteCommentTemp(commentId: String, refresh: () -> Unit) {
        viewModelScope.launch {
            repository.deletePost(commentId).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                    ) { deleteCommentTemp(commentId, refresh) })
                }
            }
        }
    }

    fun replyCommentTemp(replyBody: String, replyTo: String, refresh: () -> Unit) {
        viewModelScope.launch {
            repository.replyToComment(replyBody, _responseArticle.value!!, replyTo)
                .let { dataWrapper ->
                    when (dataWrapper) {
                        is DataWrapper.Success -> refresh.invoke()
                        is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                        ) { replyCommentTemp(replyBody, replyTo, refresh) })
                    }
                }
        }
    }

    fun addCommentWithPaging(commentBody: String, refresh: () -> Unit) {
        viewModelScope.launch {
            repository.addComment(commentBody, _responseArticle.value!!).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> refresh.invoke()
                    is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                    ) { addCommentWithPaging(commentBody, refresh) })
                }
            }
        }
    }
}