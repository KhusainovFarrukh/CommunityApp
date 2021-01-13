package khusainov.farrukh.articlestats.repo

import khusainov.farrukh.articlestats.api.RetrofitInstance
import khusainov.farrukh.articlestats.model.Article
import retrofit2.Response

class Repository {

    suspend fun getArticle(articleId: String): Response<Article> {
        return RetrofitInstance.communityApi.getArticle(articleId)
    }
}