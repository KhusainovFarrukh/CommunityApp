package khusainov.farrukh.articlestats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel : ViewModel() {

    private val repo = Repository()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _responseArticle = MutableLiveData<Response<Article>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val responseArticle: LiveData<Response<Article>> = _responseArticle

    fun getArticle(articleId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            _responseArticle.postValue(repo.getArticle(articleId))

            _isLoading.postValue(false)
        }
    }
}