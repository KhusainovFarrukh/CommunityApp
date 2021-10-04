package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by FarrukhKhusainov on 3/22/21 11:33 PM
 **/
class TopicViewModel(private val topicId: String, private val repository: Repository) :
	ViewModel() {

	/**
	[_isLoading] - topic loading state
	[_topicLiveData] - topic value
	[_sortBy] - string for sorting posts of topic
	[_errorTopic] - error while initializing topic
	[topicPostsLiveData] - posts of topic value
	 */

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