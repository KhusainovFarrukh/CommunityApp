package khusainov.farrukh.communityapp.vm.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.DataWrapper
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/22/21 11:33 PM
 **/
class TopicViewModel(private val topicId: String, private val repository: Repository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseTopic = MutableLiveData<Topic>()
    private val _sortBy = MutableLiveData<String>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseTopic: LiveData<Topic> = _responseTopic
    val responseTopicPosts = _sortBy.switchMap {
        repository.getPostsOfTopic(topicId, it).cachedIn(viewModelScope)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            repository.getTopic(topicId).let { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Success -> _responseTopic.postValue(dataWrapper.data)
                    is DataWrapper.Error -> Log.wtf("error", dataWrapper.message)
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun sortPosts(sortBy: String) {
        _sortBy.value = sortBy
    }
}