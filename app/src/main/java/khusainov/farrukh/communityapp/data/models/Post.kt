package khusainov.farrukh.communityapp.data.models

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.ISODateTimeFormat

//data class for using as Article and Comment
data class Post(
    @SerializedName("_id")
    val id: String,
    val title: String?,
    val createdAt: String,
    @SerializedName("images")
    val imagesList: List<ImagesInPost>,
    @SerializedName("counts")
    val stats: Stats,
    val content: String,
    val user: User?,
    val url: String,
    val parent: JsonElement,
    val replyTo: String?,
    val summary: String,
    val topics: List<Topic>,
    @SerializedName("upvoted")
    val isLiked: Boolean,
    val responses: JsonArray,
    @SerializedName("upvotes")
    val likes: List<User>,
) {
    fun onlyParentId() = parent.isJsonPrimitive
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

data class ImagesInPost(
    @SerializedName("src")
    val imageLink: String,
)