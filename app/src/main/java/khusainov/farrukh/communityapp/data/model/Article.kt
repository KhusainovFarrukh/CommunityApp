package khusainov.farrukh.communityapp.data.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    @SerializedName("images")
    val imagesList: List<ImagesInArticle>,
    @SerializedName("counts")
    val stats: StatsInArticle,
    val content: String,
    val user: UserModel?,
    val url: String,
    val parent: Article,
    val summary: String,
    val topics: List<Topic>
)

data class ArticleDetails(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    @SerializedName("images")
    val imagesList: List<ImagesInArticle>,
    @SerializedName("counts")
    val stats: StatsInArticle,
    val responses: List<String>?,
    @SerializedName("upvotes")
    val likes: List<UserModel>,
    val content: String,
    val user: UserModel?,
    val url: String,
    val summary: String,
    val topics: List<Topic>,
    @SerializedName("upvoted")
    var isLiked: Boolean
)

data class ArticleDetailsWithResponses(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    @SerializedName("images")
    val imagesList: List<ImagesInArticle>,
    @SerializedName("counts")
    val stats: StatsInArticle,
    val responses: JsonArray,
    @SerializedName("upvotes")
    val likes: List<UserModel>,
    val content: String,
    val user: UserModel?,
    val url: String,
    val summary: String,
    val topics: List<Topic>,
    @SerializedName("upvoted")
    var isLiked: Boolean
) {
    fun onlyResponsesId(): Boolean {
        responses.asJsonArray.let {
            return if (it.size() > 0) {
                it[0].isJsonPrimitive
            } else {
                true
            }
        }
    }
}

data class ImagesInArticle(
    @SerializedName("src")
    val imageLink: String
)

data class StatsInArticle(
    @SerializedName("downvotes")
    val dislikesCount: Int,
    @SerializedName("upvotes")
    val likesCount: Int,
    @SerializedName("followers")
    val followersCount: Int,
    @SerializedName("views")
    val viewsCount: Int,
    @SerializedName("responses")
    val commentsCount: Int
)