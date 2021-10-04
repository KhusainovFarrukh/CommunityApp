package khusainov.farrukh.communityapp.data.comments.remote

import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by farrukh_kh on 10/4/21 11:10 PM
 *khusainov.farrukh.communityapp.data.comments.remote
 **/
data class AddCommentRequest(
	val content: String,
	val type: String = "response",
	val parent: Post,
	val responsesOrder: String = "asc",
	val files: List<Any> = emptyList(),
	val images: List<Any> = emptyList(),
	val videos: List<Any> = emptyList(),
	val links: List<Any> = emptyList(),
	val noPreview: Boolean = false,
	val group: String = "main-feed",
	val topics: List<Any> = emptyList(),
)

//data class to send POST request for 'Reply to comment' functionality
data class AddSubCommentRequest(
	val content: String,
	val type: String = "response",
	val parent: Post,
	val replyTo: String,
	val responsesOrder: String = "asc",
	val files: List<Any> = emptyList(),
	val images: List<Any> = emptyList(),
	val videos: List<Any> = emptyList(),
	val links: List<Any> = emptyList(),
	val noPreview: Boolean = false,
	val group: String = "main-feed",
	val topics: List<Any> = emptyList(),
)
