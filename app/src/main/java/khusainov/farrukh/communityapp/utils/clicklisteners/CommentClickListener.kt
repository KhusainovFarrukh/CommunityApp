package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by FarrukhKhusainov on 3/12/21 12:26 AM
 **/
interface CommentClickListener {
	fun onLikeCommentClick(comment: Post)
	fun onLikeSubCommentClick(comment: Post)
	fun onWriteSubCommentClick(body: String, replyTo: String)
	fun onCommentAuthorClick(userId: String)
	fun getUserId(): String
	fun showReportDialog(commentId: String)
	fun onDeleteCommentClick(commentId: String)
	fun onDeleteSubCommentClick(commentId: String)
}