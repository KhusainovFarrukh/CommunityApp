package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.model.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticlesListViewModel : ViewModel() {

    private val repo = Repository(RetrofitInstance.communityApi)
    private val _isLoadingArticles = MutableLiveData<Boolean>()
    private val _isLoadingTopics = MutableLiveData<Boolean>()
    private val _responseAllPosts = MutableLiveData<Response<List<Article>>>()
    private val _responseTopics = MutableLiveData<Response<List<Topic>>>()

    val isLoadingArticles: LiveData<Boolean> = _isLoadingArticles
    val isLoadingTopics: LiveData<Boolean> = _isLoadingTopics
    val responseAllPosts: LiveData<Response<List<Article>>> = _responseAllPosts
    val responseTopics: LiveData<Response<List<Topic>>> = _responseTopics

    init {
        viewModelScope.launch {
            _isLoadingArticles.postValue(true)

            _responseAllPosts.postValue(repo.getAllPosts(20))

            _isLoadingArticles.postValue(false)
        }

        viewModelScope.launch {
            _isLoadingTopics.postValue(true)

            _responseTopics.postValue(repo.getTopics())

            _isLoadingTopics.postValue(false)
        }
    }
}