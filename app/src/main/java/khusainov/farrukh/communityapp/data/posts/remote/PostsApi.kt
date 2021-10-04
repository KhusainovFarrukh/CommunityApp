package khusainov.farrukh.communityapp.data.posts.remote

import khusainov.farrukh.communityapp.data.models.DeleteResponse
import khusainov.farrukh.communityapp.data.models.ReportValue
import retrofit2.http.*

/**
 *Created by farrukh_kh on 10/4/21 11:20 PM
 *khusainov.farrukh.communityapp.data.articles.remote
 **/
interface PostsApi {

	//function to get last articles (with paging)
	@GET("api/v1/posts?type=article")
	suspend fun getArticlesList(
		@Query("page") page: Int = 1,
		@Query("limit") limit: Int = 25,
	): List<Post>

	//function to get a article
	@GET("api/v1/posts/{articleId}")
	suspend fun getArticle(
		@Path("articleId") articleId: String,
	): Post

	//function to delete post
	@DELETE("api/v1/posts/{postId}")
	suspend fun deletePost(
		@Path("postId") postId: String,
	): DeletePostResponse

	//function to report a article
	@POST("api/v1/posts/{postId}/reports")
	suspend fun reportPost(
		@Path("postId") postId: String,
		@Body request: ReportPostRequest,
	): ReportPostResponse

	//function to like a post
	@POST("api/v1/posts/{postId}/votes")
	suspend fun likePost(
		@Path("postId") postId: String,
		@Body request: LikePostRequest = LikePostRequest(1),
	): Post

	//function to remove a like
	@DELETE("api/v1/posts/{postId}/votes")
	suspend fun removeLikePost(
		@Path("postId") postId: String,
	): Post
}