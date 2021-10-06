package khusainov.farrukh.communityapp.data.user.remote

import com.google.gson.annotations.SerializedName
import khusainov.farrukh.communityapp.data.utils.models.Stats

/**
 *Created by farrukh_kh on 10/4/21 11:00 PM
 *khusainov.farrukh.communityapp.data.user.remote
 **/
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