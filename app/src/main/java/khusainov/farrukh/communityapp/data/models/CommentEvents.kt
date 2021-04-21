package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 4/9/21 9:47 PM
 **/
sealed class CommentEvents {
    data class Like(val commentToLike: Post) : CommentEvents()
    data class Delete(val commentId: String) : CommentEvents()
    data class Reply(val parentComment: Post, val replyBody: String) : CommentEvents()
    data class Add(val commentBody: String) : CommentEvents()
}
