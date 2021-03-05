package khusainov.farrukh.communityapp.data.repository

import khusainov.farrukh.communityapp.data.api.CommunityApi
import khusainov.farrukh.communityapp.data.model.SignInData

class Repository(private val api: CommunityApi) {

    suspend fun getAllPosts(limit: Int) = api.getAllPosts(limit)

    suspend fun getTopics() = api.getTopics("collection")

    suspend fun getArticle(articleId: String) = api.getArticleById(articleId)

    suspend fun getUser(userId: String) = api.getUserById(userId)

    suspend fun signIn(signInData: SignInData) = api.signInWithEmail(signInData)

    suspend fun getNotifications(cookie1: String, cookie2: String) =
        api.getNotifications(cookie1, cookie2, 50)
}