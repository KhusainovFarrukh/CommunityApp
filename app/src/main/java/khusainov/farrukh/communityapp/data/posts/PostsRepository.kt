package khusainov.farrukh.communityapp.data.posts

import androidx.paging.*
import khusainov.farrukh.communityapp.data.DataWrapper
import khusainov.farrukh.communityapp.data.posts.remote.*
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/4/21 11:27 PM
 *khusainov.farrukh.communityapp.data.posts.remote
 **/
class PostsRepository @Inject constructor(private val api: PostsApi) {

	//function to get last articles (with paging)
	fun getArticlesList() = Pager(
		PagingConfig(
			pageSize = 25,
			maxSize = 100,
			enablePlaceholders = false
		),
		pagingSourceFactory = { PostsPagingSource(api) }
	).liveData

	//function to get a article
	suspend fun getArticle(articleId: String) = try {
		DataWrapper.Success(api.getArticle(articleId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to report a article
	suspend fun reportPost(postId: String, reportPostRequest: ReportPostRequest) = try {
		DataWrapper.Success(api.reportPost(postId, reportPostRequest))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to delete post
	suspend fun deletePost(postId: String) = try {
		DataWrapper.Success(api.deletePost(postId))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}

	//function to add/remove like to/from post
	suspend fun likePost(post: Post) = if (post.isLiked) {
		try {
			DataWrapper.Success(api.removeLikePost(post.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	} else {
		try {
			DataWrapper.Success(api.likePost(post.id))
		} catch (e: Exception) {
			DataWrapper.Error(e.message.toString())
		}
	}
}