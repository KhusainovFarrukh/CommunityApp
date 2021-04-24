package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 4/25/21 1:35 AM
 **/
//Temporary data class for handling some exceptions
data class OtherError(
    val message: String,
    val retry: () -> Unit
)