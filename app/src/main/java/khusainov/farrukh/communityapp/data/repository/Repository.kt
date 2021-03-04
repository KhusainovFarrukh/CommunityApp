package khusainov.farrukh.communityapp.data.repository

import khusainov.farrukh.communityapp.data.api.CommunityApi
import khusainov.farrukh.communityapp.data.model.*
import retrofit2.Response

class Repository(private val api: CommunityApi) {

    suspend fun getArticle(articleId: String): Response<Article> {
        return api.getArticleById(articleId)
    }

    suspend fun signIn(signInData: SignInData): Response<User> {
        return api.signInWithEmail(signInData)
    }

    suspend fun getNotifications(cookie1: String, cookie2: String): Response<List<Notification>> {
        return api.getNotifications(cookie1, cookie2, 50)
    }

    suspend fun getAllPosts(limit: Int): Response<List<Article>> {
        return api.getAllPosts(limit)
    }

    suspend fun getTopics(): Response<List<Topic>> {
        return api.getTopics("collection")
    }
}