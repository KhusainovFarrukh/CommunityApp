package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/22/21 11:33 PM
 **/
class TopicViewModel(private val topicId: String, private val repository: Repository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseTopic = MutableLiveData<Response<Topic>>()
    private val _sortBy = MutableLiveData<String>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseTopic: LiveData<Response<Topic>> = _responseTopic
    val responseTopicPosts = _sortBy.switchMap {
        repository.getPostsOfTopic(topicId, it).cachedIn(viewModelScope)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            _responseTopic.postValue(repository.getTopic(topicId))

            _isLoading.postValue(false)
        }
    }

    fun sortPosts(sortBy: String) {
        _sortBy.value = sortBy
    }
}