package khusainov.farrukh.communityapp.data.auth.remote

/**
 *Created by farrukh_kh on 10/4/21 10:41 PM
 *khusainov.farrukh.communityapp.data.auth.remote
 **/
//data class to send POST request for 'Sign in user' functionality
data class SignInRequest(
	val email: String,
	val password: String,
)