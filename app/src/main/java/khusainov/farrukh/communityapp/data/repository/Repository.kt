package khusainov.farrukh.communityapp.data.repository

import android.util.Log
import khusainov.farrukh.communityapp.data.api.CommunityApi
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.data.model.SignInData

class Repository(private val api: CommunityApi) {

    suspend fun getArticlesList(limit: Int) = api.getArticlesList(limit)

    suspend fun getTopics() = api.getTopics()

    suspend fun getArticleById(articleId: String) = api.getArticleById(articleId)

    suspend fun getComments(idList: List<String>): List<ArticleDetails> {
        val comments = mutableListOf<ArticleDetails>()
        idList.forEach {
            comments.add(api.getArticleById(it).body()!!)
        }
        return comments
    }

    suspend fun getUserById(userId: String) = api.getUserById(userId)

    suspend fun signInWithEmail(signInData: SignInData) = api.signInWithEmail(signInData)

    suspend fun getNotifications() = api.getNotifications()

    suspend fun likeArticleById(articleId: String): Boolean {
        return try {
            api.likeArticleById(articleId)
            true
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            false
        }
    }

    suspend fun removeLikeArticleById(articleId: String): Boolean {
        return try {
            api.removeLikeArticleById(articleId)
            false
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            true
        }
    }
}