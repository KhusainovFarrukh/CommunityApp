package khusainov.farrukh.communityapp.data.notifications.remote

import com.google.gson.annotations.SerializedName
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.models.User

/**
 *Created by farrukh_kh on 10/4/21 10:53 PM
 *khusainov.farrukh.communityapp.data.notifications.remote
 **/
data class Notification(
	@SerializedName("_id")
	val id: String,
	val verb: String,
	val from: List<User>,
	@SerializedName("read")
	val isRead: Boolean,
	val objects: List<Post>,
)
