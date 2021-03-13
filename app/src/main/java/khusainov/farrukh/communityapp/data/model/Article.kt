package khusainov.farrukh.communityapp.data.model

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
) {
    fun isLiked(userId: String): Boolean {
        likes.forEach {
            if (it.userId == userId) {
                return true
            }
        }
        return false
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