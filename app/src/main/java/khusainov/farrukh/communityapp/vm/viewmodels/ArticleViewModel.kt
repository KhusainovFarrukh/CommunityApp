package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.google.gson.Gson
import khusainov.farrukh.communityapp.data.models.CommentEvents
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


    private val _comments = repository.getComments(articleId)
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

    fun onCommentEvent(commentEvents: CommentEvents) {
        val comments = _comments.value ?: return

        viewModelScope.launch {

            when (commentEvents) {

                //like
                is CommentEvents.Like -> {
                    likeCommentWithPaging(comments, commentEvents)
                }

                //delete
                is CommentEvents.Delete -> {
                    deleteCommentWithPaging(comments, commentEvents)
                }

                //TODO bug
                //reply
                is CommentEvents.Reply -> {
                    replyCommentWithPaging(comments, commentEvents)
                }

                //add
                is CommentEvents.Add -> {
                    addCommentWithPaging(comments, commentEvents)
                }
            }
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

    //TODO what if user likes 1st degree comment and 2nd degree comment
    private suspend fun likeCommentWithPaging(
        comments: PagingData<Post>,
        commentEvents: CommentEvents.Like,
    ) {
        comments.map {
            if (commentEvents.commentToLike.id == it.id) {
                if (commentEvents.commentToLike.isLiked) {
                    repository.removeLikeArticle(commentEvents.commentToLike.id)
                        .let { responseComment ->
                            when (responseComment) {
                                is DataWrapper.Success -> return@map responseComment.data.copy(
                                    responses = it.responses)
                                is DataWrapper.Error -> return@map it
                            }
                        }
                } else {
                    repository.likeArticle(commentEvents.commentToLike.id)
                        .let { responseComment ->
                            when (responseComment) {
                                is DataWrapper.Success -> return@map responseComment.data.copy(
                                    responses = it.responses
                                )
                                is DataWrapper.Error -> return@map it
                            }
                        }
                }
            } else {
                return@map it
            }
        }.let { _comments.value = it }
    }

    //TODO-6 delete not working, tested with sub comment
    private suspend fun deleteCommentWithPaging(
        comments: PagingData<Post>,
        commentEvents: CommentEvents.Delete,
    ) {
        repository.deleteArticle(commentEvents.commentId)
        comments.filter { commentEvents.commentId != it.id }
            .let { _comments.value = it }
    }

    private suspend fun replyCommentWithPaging(
        comments: PagingData<Post>,
        commentEvents: CommentEvents.Reply,
    ) {
        comments.map {
            if (commentEvents.parentComment.id == it.id) {

                repository.testAddCommentToComment(commentEvents.replyBody,
                    commentEvents.parentComment)
//                repository.addCommentToComment(commentEvents.replyBody, commentEvents.parentComment)
                    .let { responseComment ->
                        //TODO-3 bug: View is not being updated, but reply is being added
                        when (responseComment) {
                            is DataWrapper.Success -> {
                                val newResponses = it.responses.also { jsonArray ->
                                    try {
                                        jsonArray.add(Gson().toJsonTree(responseComment.data))
                                    } catch (e: Exception) {
                                        Log.e("ArticleViewModel", e.message.toString())
                                    }
                                }

                                Log.wtf("Success", newResponses.size().toString())

                                return@map it.copy(responses = newResponses)
                            }
                            is DataWrapper.Error -> {
                                Log.wtf("Error", responseComment.message)
                                return@map it
                            }
                        }
                    }
            } else {
                Log.wtf("Else", "There is no ${commentEvents.parentComment.id} post")
                return@map it
            }
        }.let { pagingData ->
            Log.wtf("Setting LiveData", pagingData.toString())
            _comments.value = pagingData
        }
    }

    private suspend fun addCommentWithPaging(
        comments: PagingData<Post>,
        commentEvents: CommentEvents.Add,
    ) {
        repository.addComment(commentEvents.commentBody,
            _responseArticle.value!!.body()!!).let { wrapper ->
            when (wrapper) {
                is DataWrapper.Success -> _comments.value =
                    comments.insertFooterItem(wrapper.data)
                is DataWrapper.Error -> Log.e("TAG", wrapper.message)
            }
        }
    }
}