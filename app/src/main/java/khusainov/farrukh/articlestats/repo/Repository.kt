package khusainov.farrukh.articlestats.repo

import khusainov.farrukh.articlestats.api.RetrofitInstance
import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.model.Notif
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import retrofit2.Response

class Repository {

    suspend fun getArticle(articleId: String): Response<Article> {
        return RetrofitInstance.communityApi.getArticle(articleId)
    }

    suspend fun signIn(signInData: SignInData): Response<User> {
        return RetrofitInstance.communityApi.signIn(signInData)
    }

    suspend fun getNotifications(cookie1: String, cookie2: String) : Response<List<Notif>> {
        return RetrofitInstance.communityApi.getNotifications(cookie1, cookie2)
    }
}