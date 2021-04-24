package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

//data class to get response of 'Get notifications' functionality
data class Notification(
    @SerializedName("_id")
    val id: String,
    val verb: String,
    val from: List<User>,
    @SerializedName("read")
    val isRead: Boolean,
    val objects: List<Post>,
)