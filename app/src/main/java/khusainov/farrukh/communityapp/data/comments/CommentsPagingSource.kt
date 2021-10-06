package khusainov.farrukh.communityapp.data.comments

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.comments.remote.CommentsApi
import khusainov.farrukh.communityapp.data.posts.remote.Post
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by farrukh_kh on 10/4/21 11:13 PM
 *khusainov.farrukh.communityapp.data.comments
 **/
class CommentsPagingSource(
	private val api: CommentsApi,
	private val articleId: String,
) : PagingSource<Int, Post>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			api.getCommentsOfArticle(
				page = position,
				limit = 25,
				articleId = articleId,
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