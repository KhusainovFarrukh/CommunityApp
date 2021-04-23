package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(repo: Repository) : ViewModel() {

    private val _isLoadingTopics = MutableLiveData<Boolean>()
    private val _responseTopics = MutableLiveData<List<Topic>>()

    val isLoadingTopics: LiveData<Boolean> = _isLoadingTopics
    val responseAllPosts = repo.getArticlesList().cachedIn(viewModelScope)
    val responseTopics: LiveData<List<Topic>> = _responseTopics

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoadingTopics.postValue(true)

            repo.getTopics().let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseTopics.postValue(dataWrapper.data)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoadingTopics.postValue(false)
        }
    }
}