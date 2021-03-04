package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.ArticleDetailsViewModel

/**
 *Created by FarrukhKhusainov on 3/4/21 11:02 PM
 **/
class ArticleDetailsVMFactory(private val articleId: String, private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleDetailsViewModel(articleId, repository) as T
    }
}