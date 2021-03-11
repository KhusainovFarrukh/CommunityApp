package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.ArticlesListViewModel

/**
 *Created by FarrukhKhusainov on 3/11/21 1:48 AM
 **/
class ArticlesListVMFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticlesListViewModel(repository) as T
    }
}