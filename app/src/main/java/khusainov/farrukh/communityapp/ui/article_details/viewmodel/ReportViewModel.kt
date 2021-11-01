package khusainov.farrukh.communityapp.ui.article_details.viewmodel

import androidx.lifecycle.*
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.data.utils.models.OtherError
import khusainov.farrukh.communityapp.data.posts.PostsRepository
import khusainov.farrukh.communityapp.data.posts.remote.ReportPostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/16/21 4:22 PM
 **/
class ReportViewModel(private val repository: PostsRepository) : ViewModel() {

	/**
	[_isLoading] - report loading state
	[_isReported] - whether report is sent or not
	[_reportError] - error while sending error
	 */

	//private mutable live data:
	private val _isLoading = MutableLiveData<Boolean>()
	private val _isReported = MutableLiveData<Boolean>()
	private val _reportError = MutableLiveData<OtherError>()

	//public immutable live data:
	val isLoading: LiveData<Boolean> get() = _isLoading
	val isReported: LiveData<Boolean> get() = _isReported
	val reportError: LiveData<OtherError> get() = _reportError

	//fun to report a post
	fun reportPost(postId: String, reportPostRequest: ReportPostRequest) {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.reportPost(postId, reportPostRequest).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _isReported.postValue(true)
					is DataWrapper.Error -> _reportError.postValue(OtherError(dataWrapper.message)
					{ reportPost(postId, reportPostRequest) })
				}
			}

			_isLoading.postValue(false)
		}
	}
}

class ReportViewModelFactory(
	private val repository: PostsRepository,
) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
			return ReportViewModel(repository) as T
		}
		throw IllegalArgumentException("$modelClass is not ReportViewModel")
	}
}