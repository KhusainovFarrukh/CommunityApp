package khusainov.farrukh.communityapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.google.gson.JsonArray
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.*

class Repository(private val apiService: CommunityApiService) {

    suspend fun signIn(signInData: SignInData) =
        apiService.signIn(signInData)

    suspend fun getTopics() =
        apiService.getTopics()

    fun getArticlesList(): LiveData<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostsPagingSource(apiService) }
        ).liveData
    }

    fun getNotifications(): LiveData<PagingData<Notification>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                maxSize = 200,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NotificationPagingSource(apiService) }
        ).liveData
    }

    suspend fun getArticle(articleId: String) =
        apiService.getArticle(articleId)

    fun getComments(articleId: String): LiveData<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 25,
                maxSize = 200,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CommentPagingSource(articleId, apiService) }
        ).liveData
    }

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

    suspend fun likeArticle(articleId: String) = try {
        DataWrapper.Success(apiService.likeArticle(articleId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun removeLikeArticle(articleId: String) = try {
        DataWrapper.Success(apiService.removeLikeArticle(articleId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun addComment(body: String, parent: Post)  = try {
        DataWrapper.Success(apiService.addComment(CommentValue(content = body, parent = parent)))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun addCommentToComment(body: String, parent: Post) = try {
        DataWrapper.Success(apiService.addCommentToComment(CommentValue(content = body, parent = parent)))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    fun testAddCommentToComment(body: String, parent: Post) = try {
        DataWrapper.Success(Post(
            "0",
            "title",
            "2021-03-29T07:08:34.014Z",
            emptyList(),
            Stats(0, 1, 0, 1, 0, 0, 0, 0),
            body,
            null,
            "url",
            Gson().toJsonTree(parent),
            "summary",
            emptyList(),
            false,
            JsonArray(),
            emptyList()
        ))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun reportArticle(articleId: String, reportValue: ReportValue) =
        apiService.reportArticle(articleId, reportValue)

    suspend fun deleteArticle(articleId: String) =
        apiService.deleteArticle(articleId)
}