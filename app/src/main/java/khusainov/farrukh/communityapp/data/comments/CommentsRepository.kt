package khusainov.farrukh.communityapp.data.comments

import androidx.paging.*
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper
import khusainov.farrukh.communityapp.data.comments.remote.*
import khusainov.farrukh.communityapp.data.posts.remote.Post
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/4/21 11:13 PM
 *khusainov.farrukh.communityapp.data.comments
 **/
class CommentsRepository @Inject constructor(private val api: CommentsApi) {

	//function to add a comment (as a post)
	suspend fun addComment(body: String, parent: Post) = try {
		DataWrapper.Success(api.addComment(AddCommentRequest(content = body, parent = parent)))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function for replying to comment(as a post)
	suspend fun replyToComment(body: String, parent: Post, replyTo: String) = try {
		DataWrapper.Success(api.replyToComment(
			AddSubCommentRequest(content = body, parent = parent, replyTo = replyTo))
		)
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
		pagingSourceFactory = { CommentsPagingSource(api, articleId) }
	).liveData
}