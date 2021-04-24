package khusainov.farrukh.communityapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.*

class Repository(private val apiService: CommunityApiService) {

    suspend fun signIn(signInData: SignInData) = try {
        DataWrapper.Success(apiService.signIn(signInData))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun getTopics() = try {
        DataWrapper.Success(apiService.getTopics())
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    fun getArticlesList(): LiveData<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 25,
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

    suspend fun getArticle(articleId: String) = try {
        DataWrapper.Success(apiService.getArticle(articleId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

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

    suspend fun getUser(userId: String) = try {
        DataWrapper.Success(apiService.getUser(userId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun followUser(userId: String) = try {
        DataWrapper.Success(apiService.followUser(userId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun unFollowUser(userId: String) = try {
        DataWrapper.Success(apiService.unFollowUser(userId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun getTopic(topicId: String) = try {
        DataWrapper.Success(apiService.getTopic(topicId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun likePost(postId: String) = try {
        DataWrapper.Success(apiService.likePost(postId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun removeLikePost(postId: String) = try {
        DataWrapper.Success(apiService.removeLikePost(postId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun addComment(body: String, parent: Post) = try {
        DataWrapper.Success(apiService.addComment(CommentValue(content = body, parent = parent)))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun replyToComment(body: String, parent: Post, replyTo: String) = try {
        DataWrapper.Success(apiService.replyToComment(SubCommentValue(content = body,
            parent = parent,
            replyTo = replyTo)))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun reportPost(postId: String, reportValue: ReportValue) = try {
        DataWrapper.Success(apiService.reportPost(postId, reportValue))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    suspend fun deletePost(postId: String) = try {
        DataWrapper.Success(apiService.deletePost(postId))
    } catch (e: Exception) {
        DataWrapper.Error(e.message.toString())
    }

    fun getPostsOfTopic(topicId: String, sortBy: String): LiveData<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 25,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostsOfTopicPagingSource(apiService, topicId, sortBy) }
        ).liveData
    }

    fun getPostsOfUser(userId: String, type: String, sortBy: String): LiveData<PagingData<Post>> {
        return Pager(
            PagingConfig(
                pageSize = 25,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostsOfUserPagingSource(apiService, userId, type, sortBy) }
        ).liveData
    }
}