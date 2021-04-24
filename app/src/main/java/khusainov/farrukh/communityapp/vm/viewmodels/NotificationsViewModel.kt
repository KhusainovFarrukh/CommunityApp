package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.repository.Repository

/**
 *Created by FarrukhKhusainov on 3/4/21 10:52 PM
 **/
class NotificationsViewModel(repository: Repository) : ViewModel() {

    val responseNotification = repository.getNotifications().cachedIn(viewModelScope)
}