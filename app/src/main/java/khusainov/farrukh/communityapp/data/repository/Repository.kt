package khusainov.farrukh.communityapp.data.repository

import khusainov.farrukh.communityapp.data.api.CommunityApi
import khusainov.farrukh.communityapp.data.model.*

class Repository(private val api: CommunityApi) {

    suspend fun signIn(signInData: SignInData) =
        api.signIn(signInData)

    suspend fun getTopics() =
        api.getTopics()

    suspend fun getArticlesList(limit: Int) =
        api.getArticlesList(limit)

    suspend fun getNotifications() =
        api.getNotifications()

    suspend fun getArticle(articleId: String) =
        api.getArticle(articleId)

    suspend fun getComments(articleId: String) =
        api.getCommentsOfArticle(articleId)

    suspend fun getUser(userId: String) =
        api.getUser(userId)

    //TODO edit this to return User data class
    suspend fun followUser(userId: String) =
        api.followUser(userId)

    //TODO edit this to return User data class
    suspend fun unFollowUser(userId: String) =
        api.unFollowUser(userId)

    suspend fun getPostsOfUser(userId: String, type: String, sortBy: String) =
        api.getPostsOfUser(userId = userId, type = type, sort = sortBy)

    suspend fun getTopic(topicId: String) =
        api.getTopic(topicId)

    suspend fun getPostsOfTopic(topicId: String, sortBy: String) =
        api.getPostsOfTopic(topicId, sortBy)

    suspend fun likeArticle(articleId: String) =
        api.likeArticle(articleId).body()!!

    suspend fun removeLikeArticle(articleId: String) =
        api.removeLikeArticle(articleId).body()!!

    suspend fun addComment(body: String, parent: ArticleDetails) =
        api.addComment(SampleAddComment(content = body, parent = parent))

    suspend fun addCommentToComment(body: String, parent: ArticleDetailsWithResponses) =
        api.addCommentToComment(SampleAddCommentToComment(content = body, parent = parent))

    suspend fun reportArticle(articleId: String, reportValue: ReportValue) =
        api.reportArticle(articleId, reportValue)

    suspend fun deleteArticle(articleId: String) =
        api.deleteArticle(articleId)
}