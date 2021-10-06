package khusainov.farrukh.communityapp.data.user

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.posts.remote.Post
import khusainov.farrukh.communityapp.data.user.remote.UserApi
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by farrukh_kh on 10/4/21 11:02 PM
 *khusainov.farrukh.communityapp.data.user
 **/
class PostsOfUserPagingSource(
	private val api: UserApi,
	private val userId: String,
	private val type: String,
	private val sortBy: String,
) : PagingSource<Int, Post>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			api.getPostsOfUser(
				page = position,
				limit = 25,
				userId = userId,
				type = type,
				sortBy = sortBy,
			).let {
				LoadResult.Page(
					data = it,
					prevKey = if (position == Constants.PAGE_STARTING_INDEX) null else position - 1,
					nextKey = if (it.isEmpty()) null else position + 1
				)
			}
		} catch (exception: Exception) {
			LoadResult.Error(exception)
		}
	}
}