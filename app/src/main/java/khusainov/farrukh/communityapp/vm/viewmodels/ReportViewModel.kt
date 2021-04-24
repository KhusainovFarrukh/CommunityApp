package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.DataWrapper
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

    val isLoading: LiveData<Boolean> get() = _isLoading
    val isReported: LiveData<Boolean> get() = _isReported

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun reportPost(postId: String, reportValue: ReportValue) {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.reportPost(postId, reportValue).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _isReported.postValue(true)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoading.postValue(false)
        }
    }
}