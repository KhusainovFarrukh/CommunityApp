package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

//data class for using as User (with profile)
data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profile")
    val profile: ProfileInUser,
    @SerializedName("followed")
    val isFollowed: Boolean,
)

data class ProfileInUser(
    @SerializedName("counts")
    val stats: Stats,
    val score: Int,
    @SerializedName("picture")
    val photo: String,
    val banner: String,
    val name: String,
    val title: String,
    val description: String,
)