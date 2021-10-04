package khusainov.farrukh.communityapp.data.posts

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.posts.remote.Post
import khusainov.farrukh.communityapp.data.posts.remote.PostsApi
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by farrukh_kh on 10/4/21 11:29 PM
 *khusainov.farrukh.communityapp.data.posts
 **/
class PostsPagingSource(private val api: PostsApi) :
	PagingSource<Int, Post>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			api.getArticlesList(page = position, limit = 25).let {
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