package khusainov.farrukh.communityapp.data.api

import khusainov.farrukh.communityapp.data.models.*
import retrofit2.http.*

interface CommunityApiService {

    //function to sign in user
    @POST("api/v1/sessions")
    suspend fun signIn(
        @Body signInData: SignInData,
    ): User

    //function to get topics (without paging)
    @GET("api/v1/topics")
    suspend fun getTopics(
        @Query("type") type: String = "collection",
    ): List<Topic>

    //function to get last articles (with paging)
    @GET("api/v1/posts?type=article")
    suspend fun getArticlesList(
        @Query("limit") limit: Int = 25,
        @Query("page") page: Int = 1,
    ): List<Post>

    //function to get notifications of signed user  (with paging)
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int = 1,
    ): List<Notification>

    //function to get a article
    @GET("api/v1/posts/{articleId}")
    suspend fun getArticle(
        @Path("articleId") articleId: String,
    ): Post

    //function to delete post
    @DELETE("api/v1/posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: String,
    ): DeleteResponse

    //function to report a article
    @POST("api/v1/posts/{postId}/reports")
    suspend fun reportPost(
        @Path("postId") postId: String,
        @Body reportValue: ReportValue,
    ): ReportResponse

    //function to get comments of article (with paging)
    @GET("api/v1/posts/{articleId}/responses")
    suspend fun getCommentsOfArticle(
        @Path("articleId") articleId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25,
    ): List<Post>

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

    //function to posts of user (with paging)
    @GET("api/v1/users/{userId}/posts")
    suspend fun getPostsOfUser(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 25,
        @Query("type") type: String,
        @Query("sort") sort: String = "createdAt.desc",
        @Query("page") page: Int = 1,
    ): List<Post>

    //function to get topic data
    @GET("api/v1/topics/{topicId}")
    suspend fun getTopic(
        @Path("topicId") topicId: String,
    ): Topic

    //function to get posts of topic (with paging)
    @GET("api/v1/topics/{topicId}/posts?limit=20")
    suspend fun getPostsOfTopic(
        @Path("topicId") topicId: String,
        @Query("sort") sortBy: String,
        @Query("limit") limit: Int = 25,
        @Query("page") page: Int = 1,
    ): List<Post>

    //function to like a post
    @POST("api/v1/posts/{postId}/votes")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Body like: LikeValue = LikeValue(1),
    ): Post

    //function to remove a like
    @DELETE("api/v1/posts/{postId}/votes")
    suspend fun removeLikePost(
        @Path("postId") postId: String,
    ): Post

    //function to add a comment (as a post)
    @POST("api/v1/posts")
    suspend fun addComment(
        @Body commentValue: CommentValue,
    ): Post

    //function to add a comment to comment (as a post)
    @POST("api/v1/posts")
    suspend fun replyToComment(
        @Body commentValue: SubCommentValue,
    ): Post
}