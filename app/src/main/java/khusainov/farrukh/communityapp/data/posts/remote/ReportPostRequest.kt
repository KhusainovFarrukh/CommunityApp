package khusainov.farrukh.communityapp.data.posts.remote

/**
 *Created by farrukh_kh on 10/4/21 11:24 PM
 *khusainov.farrukh.communityapp.data.posts.remote
 **/
//data class to send POST request for 'Report article' functionality
data class ReportPostRequest(
	val type: String,
	val description: String,
)