package khusainov.farrukh.communityapp.data.comments.remote

import khusainov.farrukh.communityapp.data.models.*
import retrofit2.http.*

/**
 *Created by farrukh_kh on 10/4/21 11:08 PM
 *khusainov.farrukh.communityapp.data.comments.remote
 **/
interface CommentsApi {

	//function to get comments of article (with paging)
	@GET("api/v1/posts/{articleId}/responses")
	suspend fun getCommentsOfArticle(
		@Path("articleId") articleId: String,
		@Query("page") page: Int = 1,
		@Query("limit") limit: Int = 25,
	): List<Post>

	//function to add a comment (as a post)
	@POST("api/v1/posts")
	suspend fun addComment(
		@Body request: AddCommentRequest,
	): Post

	//function for replying to comment(as a post)
	@POST("api/v1/posts")
	suspend fun replyToComment(
		@Body request: AddSubCommentRequest,
	): Post
}