package khusainov.farrukh.communityapp.data.repository

import android.util.Log
import khusainov.farrukh.communityapp.data.api.CommunityApi
import khusainov.farrukh.communityapp.data.model.*

class Repository(private val api: CommunityApi) {

    suspend fun getTopicById(topicId: String) = api.getTopicById(topicId)

    suspend fun getPostsOfTopic(topicId: String, sortBy: String) = api.getPostsOfTopic(topicId, sortBy)

    suspend fun getPostsOfUserById(userId: String, type: String, sortBy: String) =
        api.getPostsOfUserById(userId = userId, type = type, sort = sortBy)

    //TODO edit this to return User data class
    suspend fun followUserById(userId: String) = api.followUserById(userId)

    //TODO edit this to return User data class
    suspend fun unFollowUserById(userId: String) = api.unFollowUserById(userId)

    suspend fun signInWithEmail(signInData: SignInData) = api.signInWithEmail(signInData)

    suspend fun getTopics() = api.getTopics()

    suspend fun getArticlesList(limit: Int) = api.getArticlesList(limit)

    suspend fun getNotifications() = api.getNotifications()

    suspend fun getArticleById(articleId: String) = api.getArticleById(articleId)

    suspend fun getComments(articleId: String) = api.getCommentsOfArticle(articleId)

    suspend fun getUserById(userId: String) = api.getUserById(userId)

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

    suspend fun addComment(body: String, parent: ArticleDetails) =
        api.addComment(SampleAddComment(content = body, parent = parent))

    suspend fun addCommentToComment(body: String, parent: ArticleDetailsWithResponses) =
        api.addCommentToComment(SampleAddCommentToComment(content = body, parent = parent))

    suspend fun reportArticle(articleId: String, reportValue: ReportValue) {
        api.reportArticleById(articleId, reportValue)
    }

    suspend fun deleteArticleById(articleId: String) = api.deleteArticleById(articleId)
}