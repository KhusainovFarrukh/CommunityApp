package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("_id")
    val topicId: String,
    val name: String,
    @SerializedName("counts")
    val stats: StatsInTopic,
    val picture: String
)

data class StatsInTopic(
    val posts: Int,
    val followers: Int
)