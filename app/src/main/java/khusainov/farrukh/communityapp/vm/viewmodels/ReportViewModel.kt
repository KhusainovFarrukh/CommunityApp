package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.OtherError
import khusainov.farrukh.communityapp.data.models.ReportValue
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/16/21 4:22 PM
 **/
class ReportViewModel(private val repository: Repository) : ViewModel() {

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
	fun reportPost(postId: String, reportValue: ReportValue) {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.reportPost(postId, reportValue).let { dataWrapper ->
				when (dataWrapper) {
                    is DataWrapper.Success -> _isReported.postValue(true)
                    is DataWrapper.Error -> _reportError.postValue(OtherError(dataWrapper.message)
                    { reportPost(postId, reportValue) })
				}
			}

			_isLoading.postValue(false)
		}
	}
}