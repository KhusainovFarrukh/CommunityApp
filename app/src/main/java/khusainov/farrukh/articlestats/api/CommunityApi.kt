package khusainov.farrukh.articlestats.api

import khusainov.farrukh.articlestats.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommunityApi {

    @GET("api/v1/posts/{articleId}")
    suspend fun getArticle(
        @Path("articleId") articleId: String
    ) : Response<Article>
}