package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.UserViewModel

/**
 *Created by FarrukhKhusainov on 3/5/21 3:02 PM
 **/
class UserVMFactory(private val userId: String, private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userId, repository) as T
    }
}