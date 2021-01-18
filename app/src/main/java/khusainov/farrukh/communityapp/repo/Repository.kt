package khusainov.farrukh.communityapp.repo

import khusainov.farrukh.communityapp.api.RetrofitInstance
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.model.Notif
import khusainov.farrukh.communityapp.model.SignInData
import khusainov.farrukh.communityapp.model.User
import retrofit2.Response

class Repository {

    suspend fun getArticle(articleId: String): Response<Article> {
        return RetrofitInstance.communityApi.getArticleById(articleId)
    }

    suspend fun signIn(signInData: SignInData): Response<User> {
        return RetrofitInstance.communityApi.signInWithEmail(signInData)
    }

    suspend fun getNotifications(cookie1: String, cookie2: String) : Response<List<Notif>> {
        return RetrofitInstance.communityApi.getNotifications(cookie1, cookie2)
    }

    suspend fun getAllPosts(limit: Int, type: String) : Response<List<Article>> {
        return RetrofitInstance.communityApi.getAllPosts(limit, type)
    }
}