package khusainov.farrukh.articlestats.api

import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityApi {

    @GET("api/v1/posts/{articleId}")
    suspend fun getArticle(
        @Path("articleId") articleId: String
    ) : Response<Article>

    @POST("api/v1/sessions")
    suspend fun signIn(
        @Body signInData: SignInData
    ) : Response<User>
}