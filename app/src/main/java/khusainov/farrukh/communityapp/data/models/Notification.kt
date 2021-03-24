package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

//data class to get response of 'Get notifications' functionality
data class Notification(
    @SerializedName("_id")
    val notificationId: String,
    val verb: String,
    val from: List<User>,
    val read: Boolean,
    val objects: List<Post>,
)