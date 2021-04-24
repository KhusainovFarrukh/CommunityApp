package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 4/25/21 1:35 AM
 **/
data class OtherError(
    val error: String,
    val retry: () -> Unit
)