package khusainov.farrukh.communityapp.ui.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.user.UserRepository
import khusainov.farrukh.communityapp.di.*
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
class PostsOfUserViewModel @Inject constructor(
	@UserId userId: String,
	@Type type: String,
	@SortBy sortBy: String,
	repository: UserRepository,
) : ViewModel() {

	//posts of user value
	val userPostsLiveData = repository.getPostsOfUser(userId, type, sortBy).cachedIn(viewModelScope)
}