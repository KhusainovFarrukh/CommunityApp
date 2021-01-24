package khusainov.farrukh.communityapp.api

import khusainov.farrukh.communityapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface CommunityApi {

    @GET("api/v1/posts/{articleId}")
    suspend fun getArticleById(
        @Path("articleId") articleId: String
    ): Response<Article>

    @POST("api/v1/sessions")
    suspend fun signInWithEmail(
        @Body signInData: SignInData,
    ): Response<User>

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Header("Cookie") cookie1: String,
        @Header("Cookie") cookie2: String,
        @Query("limit") limit: Int
    ): Response<List<Notification>>

    @GET("api/v1/posts?type=article")
    suspend fun getAllPosts(
        @Query("limit") limit: Int
    ): Response<List<Article>>

    @GET("api/v1/topics")
    suspend fun getTopics(
        @Query("type") type: String
    ): Response<List<Topic>>
}