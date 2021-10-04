package khusainov.farrukh.communityapp.data.topics

import androidx.paging.*
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.topics.remote.TopicsApi

/**
 *Created by farrukh_kh on 10/4/21 10:48 PM
 *khusainov.farrukh.communityapp.data.topics
 **/
class TopicsRepository(private val api: TopicsApi) {

	//function to get topics (without paging)
	suspend fun getTopics() = try {
		DataWrapper.Success(api.getTopics())
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get topic data
	suspend fun getTopic(topicId: String) = try {
		DataWrapper.Success(api.getTopic(topicId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get posts of topic (with paging)
	fun getPostsOfTopic(topicId: String, sortBy: String) = Pager(
		PagingConfig(
			pageSize = 25,
			maxSize = 100,
			enablePlaceholders = false
		),
		pagingSourceFactory = { PostsOfTopicPagingSource(api, topicId, sortBy) }
	).liveData
}