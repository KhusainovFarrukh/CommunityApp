package khusainov.farrukh.communityapp.data.topics

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.topics.remote.TopicsApi
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by farrukh_kh on 10/4/21 10:51 PM
 *khusainov.farrukh.communityapp.data.topics
 **/
class PostsOfTopicPagingSource(
	private val api: TopicsApi,
	private val topicId: String,
	private val sortBy: String,
) :
	PagingSource<Int, Post>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			api.getPostsOfTopic(
				page = position,
				limit = 25,
				topicId = topicId,
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