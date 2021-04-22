package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.models.Post
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

    //    private val _isLoadingPosts = MutableLiveData<Boolean>()
    private val _responseTopic = MutableLiveData<Response<Topic>>()
    private var _responseTopicPosts = repository.getPostsOfTopicWithPaging(topicId, "createdAt.desc").cachedIn(viewModelScope)

    val isLoading: LiveData<Boolean> = _isLoading

    //    val isLoadingPosts: LiveData<Boolean> = _isLoadingPosts
    val responseTopic: LiveData<Response<Topic>> = _responseTopic
    val responseTopicPosts: LiveData<PagingData<Post>> = _responseTopicPosts

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            _responseTopic.postValue(repository.getTopic(topicId))

            _isLoading.postValue(false)
        }
//        getPostsOfTopic()
    }

    fun getPostsOfTopic(sortBy: String = "createdAt.desc") {
        _responseTopicPosts =
            repository.getPostsOfTopicWithPaging(topicId, sortBy).cachedIn(viewModelScope).let {
                it as MutableLiveData<PagingData<Post>>
            }
    }
}