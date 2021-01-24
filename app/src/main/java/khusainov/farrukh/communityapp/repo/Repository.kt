package khusainov.farrukh.communityapp.repo

import khusainov.farrukh.communityapp.api.RetrofitInstance
import khusainov.farrukh.communityapp.model.*
import retrofit2.Response

class Repository {

    suspend fun getArticle(articleId: String): Response<Article> {
        return RetrofitInstance.communityApi.getArticleById(articleId)
    }

    suspend fun signIn(signInData: SignInData): Response<User> {
        return RetrofitInstance.communityApi.signInWithEmail(signInData)
    }

    suspend fun getNotifications(cookie1: String, cookie2: String): Response<List<Notification>> {
        return RetrofitInstance.communityApi.getNotifications(cookie1, cookie2, 50)
    }

    suspend fun getAllPosts(limit: Int): Response<List<Article>> {
        return RetrofitInstance.communityApi.getAllPosts(limit)
    }

    suspend fun getTopics(): Response<List<Topic>> {
        return RetrofitInstance.communityApi.getTopics("collection")
    }
}