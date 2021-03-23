package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.models.ReportValue
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/16/21 4:22 PM
 **/
class ReportViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun reportArticle(articleId: String, reportValue: ReportValue) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            repository.reportArticle(articleId, reportValue)

            _isLoading.postValue(false)
        }
    }
}