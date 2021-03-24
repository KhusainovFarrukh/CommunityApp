package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 3/14/21 4:40 PM
 **/
//data class to send POST request for 'Add comment' functionality
data class CommentValue(
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