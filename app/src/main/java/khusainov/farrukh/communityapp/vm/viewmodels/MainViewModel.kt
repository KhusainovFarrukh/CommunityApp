package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository) : ViewModel() {

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
	val articlesLiveData = repo.getArticlesList().cachedIn(viewModelScope)

	init {
		initTopics()
	}

	fun initTopics() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoadingTopics.postValue(true)

			repo.getTopics().let { dataWrapper ->
				when (dataWrapper) {
                    is DataWrapper.Success -> _topicsLiveData.postValue(dataWrapper.data)
                    is DataWrapper.Error -> _errorTopics.postValue(dataWrapper.message)
				}
			}

			_isLoadingTopics.postValue(false)
		}
	}
}