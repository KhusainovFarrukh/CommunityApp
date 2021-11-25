package khusainov.farrukh.communityapp.data.posts.remote

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import khusainov.farrukh.communityapp.data.Stats
import khusainov.farrukh.communityapp.data.topics.remote.Topic
import khusainov.farrukh.communityapp.data.user.remote.User
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.ISODateTimeFormat

/**
 *Created by farrukh_kh on 10/4/21 11:22 PM
 *khusainov.farrukh.communityapp.data.posts.remote
 **/
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
	val replyTo: JsonElement,
	val summary: String,
	val topics: List<Topic>,
	@SerializedName("upvoted")
	val isLiked: Boolean,
	val responses: JsonArray,
	@SerializedName("upvotes")
	val likes: List<User>,
	val type: String,
) {
	//Some requests return only id of parent post
	fun onlyParentId() = parent.isJsonPrimitive

	//Some requests return only ids of responses
	fun onlyResponsesId(): Boolean {
		responses.asJsonArray.let {
			return if (it.size() > 0) {
				it[0].isJsonPrimitive
			} else {
				true
			}
		}
	}

	//fun to calculate written time of post
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
			else -> "yaqinda"
		}
	}
}

data class ImagesInPost(
	@SerializedName("src")
	val imageLink: String,
)