package khusainov.farrukh.communityapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profile")
    val profileInUser: ProfileInUser
)

data class ProfileInUser(
    @SerializedName("counts")
    val statsInUser: StatsInUser,
    val score: Int,
    val picture: String,
    val banner: String,
    val name: String,
    val title: String,
    val description: String
)

data class StatsInUser(
    @SerializedName("receivedLikes")
    val likes: Int,
    @SerializedName("replies")
    val comments: Int,
    val groups: Int,
    val posts: Int,
    val followings: Int,
    val followers: Int
)

data class SignInData(
    val email: String,
    val password: String
)