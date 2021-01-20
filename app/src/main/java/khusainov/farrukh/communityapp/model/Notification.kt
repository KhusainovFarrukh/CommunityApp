package khusainov.farrukh.communityapp.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("_id")
    val notificationId: String,
    val verb: String,
    val from: List<UserModel>,
    val isRead: Boolean,
    val objects: List<Article>
)