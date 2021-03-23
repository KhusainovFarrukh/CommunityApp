package khusainov.farrukh.communityapp.data.api

import khusainov.farrukh.communityapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface CommunityApi {

    @GET("api/v1/topics/{topicId}")
    suspend fun getTopicById(
        @Path("topicId") topicId: String
    ): Response<Topic>

    @GET("api/v1/topics/{topicId}/posts?limit=20")
    suspend fun getPostsOfTopic(
        @Path("topicId") topicId: String,
        @Query("sort") sortBy: String
    ): Response<List<Article>>

    @POST("api/v1/users/{userId}/followers")
    suspend fun followUserById(
        @Path("userId") userId: String
    )

    //TODO edit this to return User data class
    @DELETE("api/v1/users/{userId}/followers")
    suspend fun unFollowUserById(
        @Path("userId") userId: String
    )

    //TODO edit this to return User data class
    @GET("api/v1/users/{userId}/posts")
    suspend fun getPostsOfUserById(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 50,
        @Query("type") type: String,
        @Query("sort") sort: String = "upvotes"
    ): Response<List<Article>>

    @DELETE("api/v1/posts/{articleId}")
    suspend fun deleteArticleById(
        @Path("articleId") articleId: String
    )

    //function to report a article
    @POST("api/v1/posts/{articleId}/reports")
    suspend fun reportArticleById(
        @Path("articleId") articleId: String,
        @Body reportValue: ReportValue
    )

    //function to sign in user
    @POST("api/v1/sessions")
    suspend fun signInWithEmail(
        @Body signInData: SignInData,
    ): Response<User>

    //function to get topics
    @GET("api/v1/topics")
    suspend fun getTopics(
        @Query("type") type: String = "collection"
    ): Response<List<Topic>>

    //function to get last 20 articles
    @GET("api/v1/posts?type=article")
    suspend fun getArticlesList(
        @Query("limit") limit: Int = 20
    ): Response<List<Article>>

    //function to get notifications of signed user
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int = 50
    ): Response<List<Notification>>

    //function to get a article
    @GET("api/v1/posts/{articleId}")
    suspend fun getArticleById(
        @Path("articleId") articleId: String
    ): Response<ArticleDetails>

    //function to get comments of article
    @GET("api/v1/posts/{articleId}/responses")
    suspend fun getCommentsOfArticle(
        @Path("articleId") articleId: String
    ): Response<List<ArticleDetailsWithResponses>>

    //function to get a user
    @GET("api/v1/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: String
    ): Response<User>

    //function to like a article
    @POST("api/v1/posts/{articleId}/votes")
    suspend fun likeArticleById(
        @Path("articleId") articleId: String,
        @Body like: LikeValue = LikeValue(1)
    ): Response<ArticleDetails>

    //function to remove a like
    @DELETE("api/v1/posts/{articleId}/votes")
    suspend fun removeLikeArticleById(
        @Path("articleId") articleId: String
    ): Response<ArticleDetails>

    //function to add a comment (as a post)
    @POST("api/v1/posts")
    suspend fun addComment(
        @Body comment: SampleAddComment
    ): Response<ArticleDetailsWithResponses>

    //function to add a comment to comment (as a post)
    @POST("api/v1/posts")
    suspend fun addCommentToComment(
        @Body comment: SampleAddCommentToComment
    ): Response<ArticleDetailsWithResponses>
}