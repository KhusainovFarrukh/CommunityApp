package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.model.Topic
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/22/21 11:33 PM
 **/
class TopicViewModel(private val repository: Repository, private val topicId: String) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isLoadingPosts = MutableLiveData<Boolean>()
    private val _responseTopic = MutableLiveData<Response<Topic>>()
    private val _responseTopicPosts = MutableLiveData<Response<List<Article>>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isLoadingPosts: LiveData<Boolean> = _isLoadingPosts
    val responseTopic: LiveData<Response<Topic>> = _responseTopic
    val responseTopicPosts: LiveData<Response<List<Article>>> = _responseTopicPosts

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseTopic.postValue(repository.getTopic(topicId))

            _isLoading.postValue(false)
        }
        getPostsOfTopic()
    }

    fun getPostsOfTopic(sortBy: String = "createdAt.desc") {
        viewModelScope.launch {
            _isLoadingPosts.postValue(true)

            _responseTopicPosts.postValue(repository.getPostsOfTopic(topicId, sortBy))

            _isLoadingPosts.postValue(false)
        }
    }
}