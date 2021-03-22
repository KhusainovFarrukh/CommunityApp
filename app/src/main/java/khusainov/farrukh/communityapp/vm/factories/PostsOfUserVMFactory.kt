package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.PostsOfUserViewModel

/**
 *Created by FarrukhKhusainov on 3/18/21 9:58 PM
 **/
class PostsOfUserVMFactory(
    private val userId: String,
    private val type: String,
    private val sortBy: String,
    private val repository: Repository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostsOfUserViewModel(userId, type, sortBy, repository) as T
    }
}