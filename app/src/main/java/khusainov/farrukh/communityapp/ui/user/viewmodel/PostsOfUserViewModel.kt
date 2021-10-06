package khusainov.farrukh.communityapp.ui.user.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import khusainov.farrukh.communityapp.data.user.UserRepository

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
class PostsOfUserViewModel(
	userId: String,
	type: String,
	sortBy: String,
	repository: UserRepository,
) : ViewModel() {

	//posts of user value
	val userPostsLiveData = repository.getPostsOfUser(userId, type, sortBy).cachedIn(viewModelScope)
}

class PostsOfUserViewModelFactory(
	private val userId: String,
	private val type: String,
	private val sortBy: String,
	private val repository: UserRepository,
) : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(PostsOfUserViewModel::class.java)) {
			return PostsOfUserViewModel(userId, type, sortBy, repository) as T
		}
		throw IllegalArgumentException("$modelClass is not PostsOfUserViewModel")
	}
}