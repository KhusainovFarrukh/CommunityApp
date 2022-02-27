package khusainov.farrukh.communityapp.ui.user.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import khusainov.farrukh.communityapp.data.user.UserRepository
import khusainov.farrukh.communityapp.utils.Constants.KEY_SORT_BY
import khusainov.farrukh.communityapp.utils.Constants.KEY_TYPE
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID
import javax.inject.Inject

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
@HiltViewModel
class PostsOfUserViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	repository: UserRepository,
) : ViewModel() {

	//posts of user value
	val userPostsLiveData = repository.getPostsOfUser(
		savedStateHandle.get<String>(KEY_USER_ID)!!,
		savedStateHandle.get<String>(KEY_TYPE)!!,
		savedStateHandle.get<String>(KEY_SORT_BY)!!
	).cachedIn(viewModelScope)
}