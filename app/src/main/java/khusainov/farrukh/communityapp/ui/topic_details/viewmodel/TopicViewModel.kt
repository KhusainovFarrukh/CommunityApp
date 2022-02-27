package khusainov.farrukh.communityapp.ui.topic_details.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import khusainov.farrukh.communityapp.data.topics.TopicsRepository
import khusainov.farrukh.communityapp.data.topics.remote.Topic
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.ui.topic_details.view.TopicFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/22/21 11:33 PM
 **/
@HiltViewModel
class TopicViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val repository: TopicsRepository,
) : ViewModel() {

	/**
	[_isLoading] - topic loading state
	[_topicLiveData] - topic value
	[_sortBy] - string for sorting posts of topic
	[_errorTopic] - error while initializing topic
	[topicPostsLiveData] - posts of topic value
	 */

	private val topicId = TopicFragmentArgs.fromSavedStateHandle(savedStateHandle).topicId

	//private mutable live data:
	private val _isLoading = MutableLiveData<Boolean>()
	private val _topicLiveData = MutableLiveData<Topic>()
	private val _sortBy = MutableLiveData<String>()
	private val _errorTopic = MutableLiveData<String>()

	//public immutable live data:
	val isLoading: LiveData<Boolean> get() = _isLoading
	val topicLiveData: LiveData<Topic> get() = _topicLiveData
	val errorTopic: LiveData<String> get() = _errorTopic
	val topicPostsLiveData = _sortBy.switchMap { sortBy ->
		repository.getPostsOfTopic(topicId, sortBy).cachedIn(viewModelScope)
	}

	init {
		initTopic()
	}

	//fun to initialize topic
	fun initTopic() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.postValue(true)

			repository.getTopic(topicId).let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _topicLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _errorTopic.postValue(dataWrapper.message)
				}
			}

			_isLoading.postValue(false)
		}
	}

	//fun to sort posts of topic
	fun sortPosts(sortBy: String) {
		if (_sortBy.value != sortBy) {
			_sortBy.postValue(sortBy)
		}
	}
}