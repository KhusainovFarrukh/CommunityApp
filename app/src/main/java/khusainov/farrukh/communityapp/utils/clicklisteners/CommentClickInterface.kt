package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.model.ArticleDetailsWithResponses

/**
 *Created by FarrukhKhusainov on 3/12/21 12:26 AM
 **/
interface CommentClickInterface {
    fun onLikeCommentClick(commentId: String, isLiked: Boolean)
    fun onLikeSubCommentClick(commentId: String, isLiked: Boolean)
    fun onWriteSubCommentClick(body: String, parentComment: ArticleDetailsWithResponses)
    fun onCommentAuthorClick(userId: String)
    fun getUserId(): String
}