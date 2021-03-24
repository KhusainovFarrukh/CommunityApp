package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 3/24/21 9:49 PM
 **/
//data class to send POST request for 'Sign in user' functionality
data class SignInData(
    val email: String,
    val password: String,
)