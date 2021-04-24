package khusainov.farrukh.communityapp.vm.viewmodels

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

class MainViewModel(private val repo: Repository) : ViewModel() {

    private val _isLoadingTopics = MutableLiveData<Boolean>()
    private val _responseTopics = MutableLiveData<List<Topic>>()
    private val _errorTopics = MutableLiveData<String>()

    val isLoadingTopics: LiveData<Boolean> = _isLoadingTopics
    val responseAllPosts = repo.getArticlesList().cachedIn(viewModelScope)
    val responseTopics: LiveData<List<Topic>> = _responseTopics
    val errorTopics: LiveData<String> get() = _errorTopics

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        initializeTopics()
    }

    fun initializeTopics() {
        coroutineScope.launch {
            _isLoadingTopics.postValue(true)

            repo.getTopics().let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseTopics.postValue(dataWrapper.data)
                    is DataWrapper.Error -> _errorTopics.postValue(dataWrapper.message)
                }
            }

            _isLoadingTopics.postValue(false)
        }
    }
}