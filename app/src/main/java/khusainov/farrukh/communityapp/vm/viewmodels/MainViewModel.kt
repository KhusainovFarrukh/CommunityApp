package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(repo: Repository) : ViewModel() {

//    private val _isLoadingArticles = MutableLiveData<Boolean>()
    private val _isLoadingTopics = MutableLiveData<Boolean>()
//    private val _responseAllPosts = MutableLiveData<PagingData<Post>>()
    private val _responseTopics = MutableLiveData<Response<List<Topic>>>()

//    val isLoadingArticles: LiveData<Boolean> = _isLoadingArticles
    val isLoadingTopics: LiveData<Boolean> = _isLoadingTopics
    val responseAllPosts = repo.getArticlesList().cachedIn(viewModelScope)
    val responseTopics: LiveData<Response<List<Topic>>> = _responseTopics

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoadingTopics.postValue(true)

            _responseTopics.postValue(repo.getTopics())

            _isLoadingTopics.postValue(false)
        }
    }
}