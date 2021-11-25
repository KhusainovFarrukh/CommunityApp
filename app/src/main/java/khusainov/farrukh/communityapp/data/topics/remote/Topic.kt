package khusainov.farrukh.communityapp.data.topics.remote

import com.google.gson.annotations.SerializedName
import khusainov.farrukh.communityapp.data.Stats

/**
 *Created by farrukh_kh on 10/4/21 10:47 PM
 *khusainov.farrukh.communityapp.data.topics.remote
 **/
data class Topic(
	@SerializedName("_id")
	val id: String,
	val name: String,
	@SerializedName("counts")
	val stats: Stats,
	val picture: String,
)