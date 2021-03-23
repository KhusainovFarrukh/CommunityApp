package khusainov.farrukh.communityapp.data.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.ISODateTimeFormat

data class Article(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    val createdAt: String,
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
) {
    fun getDifference(): String {
        val formatter = ISODateTimeFormat.dateTime()
        val createdAtDate = formatter.parseDateTime(createdAt)
        val currentDate = DateTime.now()
        val difference = Period(createdAtDate, currentDate)

        return when {
            difference.years > 0 -> "${difference.years} yil avval"
            difference.months > 0 -> "${difference.months} oy avval"
            difference.weeks > 0 -> "${difference.weeks} hafta avval"
            difference.days > 0 -> "${difference.days} kun avval"
            difference.hours > 0 -> "${difference.hours} soat avval"
            difference.minutes > 0 -> "${difference.minutes} daqiqa avval"
            difference.seconds > 0 -> "${difference.seconds} soniya avval"
            else -> "hozir?"
        }
    }
}

data class ArticleDetails(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    val createdAt: String,
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
) {
    fun getDifference(): String {
        val formatter = ISODateTimeFormat.dateTime()
        val createdAtDate = formatter.parseDateTime(createdAt)
        val currentDate = DateTime.now()
        val difference = Period(createdAtDate, currentDate)

        return when {
            difference.years > 0 -> "${difference.years} yil avval"
            difference.months > 0 -> "${difference.months} oy avval"
            difference.weeks > 0 -> "${difference.weeks} hafta avval"
            difference.days > 0 -> "${difference.days} kun avval"
            difference.hours > 0 -> "${difference.hours} soat avval"
            difference.minutes > 0 -> "${difference.minutes} daqiqa avval"
            difference.seconds > 0 -> "${difference.seconds} soniya avval"
            else -> "hozir?"
        }
    }
}

data class ArticleDetailsWithResponses(
    @SerializedName("_id")
    val articleId: String,
    val title: String?,
    val createdAt: String,
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

    fun getDifference(): String {
        val formatter = ISODateTimeFormat.dateTime()
        val createdAtDate = formatter.parseDateTime(createdAt)
        val currentDate = DateTime.now()
        val difference = Period(createdAtDate, currentDate)

        return when {
            difference.years > 0 -> "${difference.years} yil avval"
            difference.months > 0 -> "${difference.months} oy avval"
            difference.weeks > 0 -> "${difference.weeks} hafta avval"
            difference.days > 0 -> "${difference.days} kun avval"
            difference.hours > 0 -> "${difference.hours} soat avval"
            difference.minutes > 0 -> "${difference.minutes} daqiqa avval"
            difference.seconds > 0 -> "${difference.seconds} soniya avval"
            else -> "hozir?"
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