package khusainov.farrukh.communityapp.data.user

import androidx.paging.*
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.data.user.remote.User
import khusainov.farrukh.communityapp.data.user.remote.UserApi
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/4/21 11:01 PM
 *khusainov.farrukh.communityapp.data.user
 **/
class UserRepository  @Inject constructor(private val api: UserApi) {

	//function to get a user
	suspend fun getUser(userId: String) = try {
		DataWrapper.Success(api.getUser(userId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to follow/unfollow user
	suspend fun followUser(user: User) = if (user.isFollowed) {
		try {
			DataWrapper.Success(api.unFollowUser(user.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	} else {
		try {
			DataWrapper.Success(api.followUser(user.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	}

	//function to get posts of user (with paging)
	fun getPostsOfUser(userId: String, type: String, sortBy: String) = Pager(
		PagingConfig(
			pageSize = 25,
			maxSize = 100,
			enablePlaceholders = false
		),
		pagingSourceFactory = { PostsOfUserPagingSource(api, userId, type, sortBy) }
	).liveData
}