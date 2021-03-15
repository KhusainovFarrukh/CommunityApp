package khusainov.farrukh.communityapp.data.model

/**
 *Created by FarrukhKhusainov on 3/14/21 4:40 PM
 **/
data class SampleAddComment(
    val content: String,
    val type: String = "response",
    val parent: ArticleDetails,
    val responsesOrder: String = "asc",
    val files: List<Any> = emptyList(),
    val images: List<Any> = emptyList(),
    val videos: List<Any> = emptyList(),
    val links: List<Any> = emptyList(),
    val noPreview: Boolean = false,
    val group: String = "main-feed",
    val topics: List<Any> = emptyList()
)

data class SampleAddCommentToComment(
    val content: String,
    val type: String = "response",
    val parent: ArticleDetailsWithResponses,
    val responsesOrder: String = "asc",
    val files: List<Any> = emptyList(),
    val images: List<Any> = emptyList(),
    val videos: List<Any> = emptyList(),
    val links: List<Any> = emptyList(),
    val noPreview: Boolean = false,
    val group: String = "main-feed",
    val topics: List<Any> = emptyList()
)