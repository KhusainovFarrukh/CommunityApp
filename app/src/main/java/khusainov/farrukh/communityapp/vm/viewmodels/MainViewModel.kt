package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(repo: Repository) : ViewModel() {

    private val _isLoadingArticles = MutableLiveData<Boolean>()
    private val _isLoadingTopics = MutableLiveData<Boolean>()
    private val _responseAllPosts = MutableLiveData<Response<List<Post>>>()
    private val _responseTopics = MutableLiveData<Response<List<Topic>>>()

    val isLoadingArticles: LiveData<Boolean> = _isLoadingArticles
    val isLoadingTopics: LiveData<Boolean> = _isLoadingTopics
    val responseAllPosts: LiveData<Response<List<Post>>> = _responseAllPosts
    val responseTopics: LiveData<Response<List<Topic>>> = _responseTopics

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoadingArticles.postValue(true)

            _responseAllPosts.postValue(repo.getArticlesList(20))

            _isLoadingArticles.postValue(false)
        }

        coroutineScope.launch {
            _isLoadingTopics.postValue(true)

            _responseTopics.postValue(repo.getTopics())

            _isLoadingTopics.postValue(false)
        }
    }
}