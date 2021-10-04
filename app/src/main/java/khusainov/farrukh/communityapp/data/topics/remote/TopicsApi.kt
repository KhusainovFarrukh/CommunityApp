package khusainov.farrukh.communityapp.data.topics.remote

import khusainov.farrukh.communityapp.data.models.Post
import retrofit2.http.*

/**
 *Created by farrukh_kh on 10/4/21 10:46 PM
 *khusainov.farrukh.communityapp.data.topics.remote
 **/
interface TopicsApi {

	//function to get topics (without paging)
	@GET("api/v1/topics")
	suspend fun getTopics(
		@Query("type") type: String = "collection",
	): List<Topic>

	//function to get topic data
	@GET("api/v1/topics/{topicId}")
	suspend fun getTopic(
		@Path("topicId") topicId: String,
	): Topic

	//function to get posts of topic (with paging)
	@GET("api/v1/topics/{topicId}/posts")
	suspend fun getPostsOfTopic(
		@Path("topicId") topicId: String,
		@Query("page") page: Int = 1,
		@Query("limit") limit: Int = 25,
		@Query("sort") sortBy: String,
	): List<Post>
}