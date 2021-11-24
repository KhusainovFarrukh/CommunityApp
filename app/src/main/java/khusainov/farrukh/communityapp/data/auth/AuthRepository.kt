package khusainov.farrukh.communityapp.data.auth

import khusainov.farrukh.communityapp.data.auth.remote.AuthApi
import khusainov.farrukh.communityapp.data.auth.remote.SignInRequest
import khusainov.farrukh.communityapp.data.utils.models.DataWrapper

/**
 *Created by farrukh_kh on 10/4/21 10:43 PM
 *khusainov.farrukh.communityapp.data.auth
 **/
class AuthRepository(private val api: AuthApi) {

	//function to sign in user
	suspend fun signIn(signInRequest: SignInRequest) = try {
		DataWrapper.Success(api.signIn(signInRequest))
	} catch (e: Exception) {
		DataWrapper.Error(e.message.toString())
	}
}