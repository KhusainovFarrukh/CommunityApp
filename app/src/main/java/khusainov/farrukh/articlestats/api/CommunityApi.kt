package khusainov.farrukh.articlestats.api

import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.model.Notif
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import okhttp3.Cookie
import retrofit2.Response
import retrofit2.http.*

interface CommunityApi {

    @GET("api/v1/posts/{articleId}")
    suspend fun getArticle(
        @Path("articleId") articleId: String
    ) : Response<Article>

    @POST("api/v1/sessions")
    suspend fun signIn(
        @Body signInData: SignInData,
    ) : Response<User>

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Header("Cookie") cookie1: String,
        @Header("Cookie") cookie2: String
    ) : Response<List<Notif>>
}