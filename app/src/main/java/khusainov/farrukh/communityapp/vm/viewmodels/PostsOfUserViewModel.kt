package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.repository.Repository

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
class PostsOfUserViewModel(
    userId: String,
    type: String,
    sortBy: String,
    repository: Repository,
) : ViewModel() {

	//posts of user value
	val userPostsLiveData = repository.getPostsOfUser(userId, type, sortBy).cachedIn(viewModelScope)
}