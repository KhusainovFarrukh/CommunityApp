package khusainov.farrukh.communityapp.data.models

import com.google.gson.annotations.SerializedName

/**
 *Created by FarrukhKhusainov on 4/25/21 12:48 AM
 **/
//data class to get POST request for 'Report post' functionality
data class ReportResponse(
    @SerializedName("_id")
    val id: String,
    val type: String,
    val description: String,
    val status: String,
)