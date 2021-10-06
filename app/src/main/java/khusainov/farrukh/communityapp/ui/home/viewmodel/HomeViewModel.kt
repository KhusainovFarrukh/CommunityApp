package khusainov.farrukh.communityapp.ui.home.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.data.posts.PostsRepository
import khusainov.farrukh.communityapp.data.topics.TopicsRepository
import khusainov.farrukh.communityapp.data.topics.remote.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
	private val topicsRepository: TopicsRepository,
	private val postsRepository: PostsRepository,
) : ViewModel() {

	/**
	[_isLoadingTopics] - topics loading state
	[_topicsLiveData] - topics value
	[_errorTopics] - error while initializing topics
	[articlesLiveData] - articles value
	 */

	//private mutable live data:
	private val _isLoadingTopics = MutableLiveData<Boolean>()
	private val _topicsLiveData = MutableLiveData<List<Topic>>()
	private val _errorTopics = MutableLiveData<String>()

	//public immutable live data:
	val isLoadingTopics: LiveData<Boolean> get() = _isLoadingTopics
	val topicsLiveData: LiveData<List<Topic>> get() = _topicsLiveData
	val errorTopics: LiveData<String> get() = _errorTopics
	val articlesLiveData = postsRepository.getArticlesList().cachedIn(viewModelScope)

	init {
		initTopics()
	}

	fun initTopics() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoadingTopics.postValue(true)

			topicsRepository.getTopics().let { dataWrapper ->
				when (dataWrapper) {
					is DataWrapper.Success -> _topicsLiveData.postValue(dataWrapper.data)
					is DataWrapper.Error -> _errorTopics.postValue(dataWrapper.message)
				}
			}

			_isLoadingTopics.postValue(false)
		}
	}
}

class HomeViewModelFactory(
	private val topicsRepository: TopicsRepository,
	private val postsRepository: PostsRepository,
) : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
			return HomeViewModel(topicsRepository, postsRepository) as T
		}
		throw IllegalArgumentException("$modelClass is not HomeViewModel")
	}
}