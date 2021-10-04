package khusainov.farrukh.communityapp.data.posts.remote

import com.google.gson.annotations.SerializedName

/**
 *Created by farrukh_kh on 10/4/21 11:25 PM
 *khusainov.farrukh.communityapp.data.posts.remote
 **/
//data class to get POST request for 'Report post' functionality
data class ReportPostResponse(
	@SerializedName("_id")
	val id: String,
	val type: String,
	val description: String,
	val status: String,
)