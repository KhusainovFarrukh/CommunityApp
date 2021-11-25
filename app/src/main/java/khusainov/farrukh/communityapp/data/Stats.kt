package khusainov.farrukh.communityapp.data

import com.google.gson.annotations.SerializedName

/**
 *Created by FarrukhKhusainov on 3/24/21 9:44 PM
 **/
//data class for using as Statistics in other data classes
data class Stats(
	@SerializedName("downvotes")
	val dislikes: Int,
	@SerializedName("upvotes")
	val likes: Int,
	@SerializedName("receivedLikes")
	val likesOfUser: Int,
	@SerializedName("followers")
	val followers: Int,
	@SerializedName("views")
	val viewsCount: Int,
	@SerializedName("responses")
	val comments: Int,
	@SerializedName("replies")
	val commentsOfUser: Int,
	val groups: Int,
	val posts: Int,
	val followings: Int,
)