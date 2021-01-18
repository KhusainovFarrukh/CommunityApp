package khusainov.farrukh.communityapp.api

import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.model.Notif
import khusainov.farrukh.communityapp.model.SignInData
import khusainov.farrukh.communityapp.model.User
import retrofit2.Response
import retrofit2.http.*
import java.lang.StringBuilder

interface CommunityApi {

    @GET("api/v1/posts/{articleId}")
    suspend fun getArticleById(
        @Path("articleId") articleId: String
    ) : Response<Article>

    @POST("api/v1/sessions")
    suspend fun signInWithEmail(
        @Body signInData: SignInData,
    ) : Response<User>

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Header("Cookie") cookie1: String,
        @Header("Cookie") cookie2: String
    ) : Response<List<Notif>>

    @GET("api/v1/posts")
    suspend fun getAllPosts(
        @Query("limit") limit: Int,
        @Query("type") type: String
    ) : Response<List<Article>>
}