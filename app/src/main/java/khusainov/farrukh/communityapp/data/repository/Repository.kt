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

    suspend fun likeArticleById(articleId: String): ArticleDetails {
        return try {
            api.likeArticleById(articleId).body()!!
        } catch (e: Exception) {
            Log.wtf("ERROR IN REPO", e.message)
            throw Exception("WTF")
        }
    }

    suspend fun removeLikeArticleById(articleId: String): ArticleDetails {
        return try {
            api.removeLikeArticleById(articleId).body()!!
        } catch (e: Exception) {
            Log.wtf("ERROR IN REPO", e.message)
            throw Exception("WTF")
        }
    }
}