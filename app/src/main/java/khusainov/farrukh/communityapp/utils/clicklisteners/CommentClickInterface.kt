package khusainov.farrukh.communityapp.utils.clicklisteners

/**
 *Created by FarrukhKhusainov on 3/12/21 12:26 AM
 **/
interface CommentClickInterface {
    fun onLikeCommentClick(commentId: String, isLiked: Boolean)
    fun onCommentAuthorClick(userId: String)
    fun onReplyCommentClick()
    fun getUserId(): String
}