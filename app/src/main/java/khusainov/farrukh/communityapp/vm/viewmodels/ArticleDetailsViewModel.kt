package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *Created by FarrukhKhusainov on 3/4/21 10:53 PM
 **/
class ArticleDetailsViewModel(articleId: String, repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<Article>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseArticle: LiveData<Response<Article>> = _responseArticle

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseArticle.postValue(repository.getArticle(articleId))

            _isLoading.postValue(false)
        }
    }
}