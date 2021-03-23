package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("_id")
    val notificationId: String,
    val verb: String,
    val from: List<UserModel>,
    val read: Boolean,
    val objects: List<Article>
)