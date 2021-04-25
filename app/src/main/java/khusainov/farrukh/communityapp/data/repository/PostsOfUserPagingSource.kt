package khusainov.farrukh.communityapp.data.repository

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by FarrukhKhusainov on 4/22/21 8:56 PM
 **/
class PostsOfUserPagingSource(
    private val communityApiService: CommunityApiService,
    private val userId: String,
    private val type: String,
    private val sortBy: String,
) : PagingSource<Int, Post>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			communityApiService.getPostsOfUser(
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