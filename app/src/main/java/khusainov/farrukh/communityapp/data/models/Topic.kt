package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

//data class for 'Get topics, Get topic details' functionalities
data class Topic(
    @SerializedName("_id")
    val id: String,
    val name: String,
    @SerializedName("counts")
    val stats: Stats,
    val picture: String,
)