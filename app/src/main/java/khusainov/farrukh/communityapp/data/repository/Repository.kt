package khusainov.farrukh.communityapp.data.repository

import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.*

class Repository(private val apiService: CommunityApiService) {

    suspend fun signIn(signInData: SignInData) =
        apiService.signIn(signInData)

    suspend fun getTopics() =
        apiService.getTopics()

    suspend fun getArticlesList(limit: Int) =
        apiService.getArticlesList(limit)

    suspend fun getNotifications() =
        apiService.getNotifications()

    suspend fun getArticle(articleId: String) =
        apiService.getArticle(articleId)

    suspend fun getComments(articleId: String) =
        apiService.getCommentsOfArticle(articleId)

    suspend fun getUser(userId: String) =
        apiService.getUser(userId)

    //TODO edit this to return User data class
    suspend fun followUser(userId: String) =
        apiService.followUser(userId)

    //TODO edit this to return User data class
    suspend fun unFollowUser(userId: String) =
        apiService.unFollowUser(userId)

    suspend fun getPostsOfUser(userId: String, type: String, sortBy: String) =
        apiService.getPostsOfUser(userId = userId, type = type, sort = sortBy)

    suspend fun getTopic(topicId: String) =
        apiService.getTopic(topicId)

    suspend fun getPostsOfTopic(topicId: String, sortBy: String) =
        apiService.getPostsOfTopic(topicId, sortBy)

    suspend fun likeArticle(articleId: String) =
        apiService.likeArticle(articleId).body()!!

    suspend fun removeLikeArticle(articleId: String) =
        apiService.removeLikeArticle(articleId).body()!!

    suspend fun addComment(body: String, parent: Post) =
        apiService.addComment(CommentValue(content = body, parent = parent))

    suspend fun addCommentToComment(body: String, parent: Post) =
        apiService.addCommentToComment(CommentValue(content = body, parent = parent))

    suspend fun reportArticle(articleId: String, reportValue: ReportValue) =
        apiService.reportArticle(articleId, reportValue)

    suspend fun deleteArticle(articleId: String) =
        apiService.deleteArticle(articleId)
}