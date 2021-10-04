package khusainov.farrukh.communityapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.*

class Repository(private val apiService: CommunityApiService) {

	//function to sign in user
	suspend fun signIn(signInData: SignInData) = try {
		DataWrapper.Success(apiService.signIn(signInData))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get topics (without paging)
	suspend fun getTopics() = try {
		DataWrapper.Success(apiService.getTopics())
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get last articles (with paging)
	fun getArticlesList() = Pager(
        PagingConfig(
            pageSize = 25,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostsPagingSource(apiService) }
    ).liveData

	//function to get notifications of signed user  (with paging)
	fun getNotifications() = Pager(
        PagingConfig(
            pageSize = 50,
            maxSize = 200,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NotificationPagingSource(apiService) }
    ).liveData

	//function to get a article
	suspend fun getArticle(articleId: String) = try {
		DataWrapper.Success(apiService.getArticle(articleId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get comments of article (with paging)
	fun getCommentsOfArticle(articleId: String) = Pager(
        PagingConfig(
            pageSize = 25,
            maxSize = 200,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CommentPagingSource(articleId, apiService) }
    ).liveData

	//function to get a user
	suspend fun getUser(userId: String) = try {
		DataWrapper.Success(apiService.getUser(userId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to follow/unfollow user
	suspend fun followUser(user: User) = if (user.isFollowed) {
		try {
			DataWrapper.Success(apiService.unFollowUser(user.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	} else {
		try {
			DataWrapper.Success(apiService.followUser(user.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	}

	//function to get topic data
	suspend fun getTopic(topicId: String) = try {
		DataWrapper.Success(apiService.getTopic(topicId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to add/remove like to/from post
	suspend fun likePost(post: Post) = if (post.isLiked) {
		try {
			DataWrapper.Success(apiService.removeLikePost(post.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	} else {
		try {
			DataWrapper.Success(apiService.likePost(post.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	}

	//function to add a comment (as a post)
	suspend fun addComment(body: String, parent: Post) = try {
		DataWrapper.Success(apiService.addComment(CommentValue(content = body, parent = parent)))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function for replying to comment(as a post)
	suspend fun replyToComment(body: String, parent: Post, replyTo: String) = try {
		DataWrapper.Success(apiService.replyToComment(
            SubCommentValue(content = body, parent = parent, replyTo = replyTo)))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to report a article
	suspend fun reportPost(postId: String, reportValue: ReportValue) = try {
		DataWrapper.Success(apiService.reportPost(postId, reportValue))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to delete post
	suspend fun deletePost(postId: String) = try {
		DataWrapper.Success(apiService.deletePost(postId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to get posts of topic (with paging)
	fun getPostsOfTopic(topicId: String, sortBy: String) = Pager(
        PagingConfig(
            pageSize = 25,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostsOfTopicPagingSource(apiService, topicId, sortBy) }
    ).liveData

	//function to get posts of user (with paging)
	fun getPostsOfUser(userId: String, type: String, sortBy: String) = Pager(
        PagingConfig(
            pageSize = 25,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostsOfUserPagingSource(apiService, userId, type, sortBy) }
    ).liveData
}