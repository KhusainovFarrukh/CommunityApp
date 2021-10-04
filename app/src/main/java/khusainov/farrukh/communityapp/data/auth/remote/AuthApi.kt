package khusainov.farrukh.communityapp.data.auth.remote

import khusainov.farrukh.communityapp.data.user.remote.User
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *Created by farrukh_kh on 10/4/21 10:40 PM
 *khusainov.farrukh.communityapp.data.auth.remote
 **/
interface AuthApi {

	//endpoint to sign in user
	@POST("api/v1/sessions")
	suspend fun signIn(
		@Body signInRequest: SignInRequest,
	): User
}