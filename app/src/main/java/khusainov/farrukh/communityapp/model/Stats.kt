package khusainov.farrukh.communityapp.model

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("upvotes")
    val likes: Int,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("responses")
    val comments: Int,
    @SerializedName("views")
    val views: Int
)