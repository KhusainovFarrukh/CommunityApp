package khusainov.farrukh.communityapp.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("_id")
    val userId: String,
    val profile: ProfileModel
)

data class ProfileModel(
    @SerializedName("picture")
    val profilePhoto: String,
    val name: String,
    val description: String?
)