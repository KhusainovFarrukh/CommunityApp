package khusainov.farrukh.communityapp.data.user.remote

import khusainov.farrukh.communityapp.data.models.Post
import retrofit2.http.*

/**
 *Created by farrukh_kh on 10/4/21 10:59 PM
 *khusainov.farrukh.communityapp.data.user.remote
 **/
interface UserApi {

	//function to get a user
	@GET("api/v1/users/{userId}")
	suspend fun getUser(
		@Path("userId") userId: String,
	): User

	//function to follow user
	@POST("api/v1/users/{userId}/followers")
	suspend fun followUser(
		@Path("userId") userId: String,
	): User

	//function to unfollow user
	@DELETE("api/v1/users/{userId}/followers")
	suspend fun unFollowUser(
		@Path("userId") userId: String,
	): User

	//function to get posts of user (with paging)
	@GET("api/v1/users/{userId}/posts")
	suspend fun getPostsOfUser(
		@Path("userId") userId: String,
		@Query("page") page: Int = 1,
		@Query("limit") limit: Int = 25,
		@Query("type") type: String,
		@Query("sort") sortBy: String = "createdAt.desc",
	): List<Post>
}