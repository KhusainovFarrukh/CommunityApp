package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.OtherError
import khusainov.farrukh.communityapp.data.models.ReportValue
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/16/21 4:22 PM
 **/
class ReportViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isReported = MutableLiveData<Boolean>()
    private val _otherError = MutableLiveData<OtherError>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val isReported: LiveData<Boolean> get() = _isReported
    val otherError: LiveData<OtherError> get() = _otherError

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun reportPost(postId: String, reportValue: ReportValue) {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.reportPost(postId, reportValue).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _isReported.postValue(true)
                    is DataWrapper.Error -> _otherError.postValue(OtherError(dataWrapper.message
                    ) { reportPost(postId, reportValue) })
                }
            }

            _isLoading.postValue(false)
        }
    }
}