package khusainov.farrukh.communityapp.data.api

import khusainov.farrukh.communityapp.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface CommunityApiService {

    //function to sign in user
    @POST("api/v1/sessions")
    suspend fun signIn(
        @Body signInData: SignInData,
    ): Response<User>

    //function to get topics
    @GET("api/v1/topics")
    suspend fun getTopics(
        @Query("type") type: String = "collection",
    ): Response<List<Topic>>

    //function to get last 20 articles
    @GET("api/v1/posts?type=article")
    suspend fun getArticlesList(
        @Query("limit") limit: Int = 20,
    ): Response<List<Post>>

    //function to get notifications of signed user
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int = 50,
    ): Response<List<Notification>>

    //function to get a article
    @GET("api/v1/posts/{articleId}")
    suspend fun getArticle(
        @Path("articleId") articleId: String,
    ): Response<Post>

    //function to delete own comment
    @DELETE("api/v1/posts/{articleId}")
    suspend fun deleteArticle(
        @Path("articleId") articleId: String,
    )

    //function to report a article
    @POST("api/v1/posts/{articleId}/reports")
    suspend fun reportArticle(
        @Path("articleId") articleId: String,
        @Body reportValue: ReportValue,
    )

    //function to get comments of article
    @GET("api/v1/posts/{articleId}/responses")
    suspend fun getCommentsOfArticle(
        @Path("articleId") articleId: String,
    ): Response<List<Post>>

    //function to get a user
    @GET("api/v1/users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
    ): Response<User>

    //TODO edit this to return User data class
    //function to follow user
    @POST("api/v1/users/{userId}/followers")
    suspend fun followUser(
        @Path("userId") userId: String,
    )

    //TODO edit this to return User data class
    //function to unfollow user
    @DELETE("api/v1/users/{userId}/followers")
    suspend fun unFollowUser(
        @Path("userId") userId: String,
    )

    //function to posts of user
    @GET("api/v1/users/{userId}/posts")
    suspend fun getPostsOfUser(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 50,
        @Query("type") type: String,
        @Query("sort") sort: String = "upvotes",
    ): Response<List<Post>>

    //function to get topic data
    @GET("api/v1/topics/{topicId}")
    suspend fun getTopic(
        @Path("topicId") topicId: String,
    ): Response<Topic>

    //function to get posts of topic
    @GET("api/v1/topics/{topicId}/posts?limit=20")
    suspend fun getPostsOfTopic(
        @Path("topicId") topicId: String,
        @Query("sort") sortBy: String,
    ): Response<List<Post>>

    //function to like a article
    @POST("api/v1/posts/{articleId}/votes")
    suspend fun likeArticle(
        @Path("articleId") articleId: String,
        @Body like: LikeValue = LikeValue(1),
    ): Response<Post>

    //function to remove a like
    @DELETE("api/v1/posts/{articleId}/votes")
    suspend fun removeLikeArticle(
        @Path("articleId") articleId: String,
    ): Response<Post>

    //function to add a comment (as a post)
    @POST("api/v1/posts")
    suspend fun addComment(
        @Body commentValue: CommentValue,
    ): Response<Post>

    //function to add a comment to comment (as a post)
    @POST("api/v1/posts")
    suspend fun addCommentToComment(
        @Body commentValue: CommentValue,
    ): Response<Post>
}