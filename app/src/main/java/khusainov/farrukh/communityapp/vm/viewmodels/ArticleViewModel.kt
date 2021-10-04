package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.models.*
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleViewModel(private val articleId: String, private val repository: Repository) :
	ViewModel() {

	/**
	[_isLoading] - article loading state
	[_articleLiveData] - article value
	[_errorArticle] - error while initializing article
	[_otherError] - error while executing some functions (like, add comment, reply ...)
	[_commentsLiveData] - comments of article
	 */

	var isFirstTime = true
	//private mutable live data:
	private val _isLoading = MutableLiveData<Boolean>()
	private val _articleLiveData = MutableLiveData<Post>()
	private val _errorArticle = MutableLiveData<String>()
	private val _otherError = MutableLiveData<OtherError>()
	private var _commentsLiveData = repository.getCommentsOfArticle(articleId)
		.cachedIn(viewModelScope)

	//public immutable live data:
	val isLoading: LiveData<Boolean> get() = _isLoading
	val articleLiveData: LiveData<Post> get() = _articleLiveData
	val errorArticle: LiveData<String> get() = _errorArticle
	val otherError: LiveData<OtherError> get() = _otherError
	val comments: LiveData<PagingData<Post>> get() = _commentsLiveData

	init {
		initArticle()
	}

	//fun for initializing article
	fun initArticle() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.getArticle(articleId).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _articleLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _errorArticle.postValue(dataWrapper.message)
				}
			}

			_isLoading.postValue(false)
		}
	}

	//fun to add/remove like to/from article
	fun likeArticle() {
		viewModelScope.launch(Dispatchers.IO) {
			repository.likePost(_articleLiveData.value!!).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _articleLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message)
					{ likeArticle() })
				}
			}
		}
	}

	//TEMPORARY! fun to like comment
	fun likeCommentTemp(comment: Post, refresh: () -> Unit) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.likePost(comment).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> refresh.invoke()
					is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message)
					{ likeCommentTemp(comment, refresh) })
				}
			}
		}
	}

	//TEMPORARY! fun to delete comment
	fun deleteCommentTemp(commentId: String, refresh: () -> Unit) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.deletePost(commentId).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> refresh.invoke()
					is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message)
					{ deleteCommentTemp(commentId, refresh) })
				}
			}
		}
	}

	//TEMPORARY! fun for replying to comment
	fun replyCommentTemp(replyBody: String, replyTo: String, refresh: () -> Unit) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.replyToComment(replyBody, _articleLiveData.value!!, replyTo)
				.let { dataWrapper ->
					when (dataWrapper) {
						is DataWrapper.Success -> refresh.invoke()
						is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message)
						{ replyCommentTemp(replyBody, replyTo, refresh) })
					}
				}
		}
	}

	//TEMPORARY! fun to add comment to article
	fun addCommentTemp(commentBody: String, refresh: () -> Unit) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.addComment(commentBody, _articleLiveData.value!!).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> refresh.invoke()
					is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message)
					{ addCommentTemp(commentBody, refresh) })
				}
			}
		}
	}
}