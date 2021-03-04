package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.NotificationsViewModel

/**
 *Created by FarrukhKhusainov on 3/4/21 11:02 PM
 **/
class NotificationsVMFactory(
    private val cookies: List<String>,
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationsViewModel(cookies, repository) as T
    }
}