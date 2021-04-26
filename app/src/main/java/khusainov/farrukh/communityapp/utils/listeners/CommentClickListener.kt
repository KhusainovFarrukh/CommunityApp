package khusainov.farrukh.communityapp.utils.listeners

import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by FarrukhKhusainov on 3/12/21 12:26 AM
 **/
interface CommentClickListener {
	fun onLikeCommentClick(comment: Post)
	fun onReplyClick(body: String, replyTo: String)
	fun onCommentAuthorClick(userId: String)
	fun onReportClick(commentId: String)
	fun onDeleteCommentClick(commentId: String)
	fun getUserId(): String
}