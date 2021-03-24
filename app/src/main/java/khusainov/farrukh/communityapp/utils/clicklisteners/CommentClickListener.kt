package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by FarrukhKhusainov on 3/12/21 12:26 AM
 **/
interface CommentClickListener {
    fun onLikeCommentClick(commentId: String, isLiked: Boolean)
    fun onLikeSubCommentClick(commentId: String, isLiked: Boolean)
    fun onWriteSubCommentClick(body: String, parentComment: Post)
    fun onCommentAuthorClick(userId: String)
    fun getUserId(): String
    fun showReportDialog(commentId: String)
    fun onDeleteCommentClick(commentId: String)
    fun onDeleteSubCommentClick(commentId: String)
}