package khusainov.farrukh.communityapp.utils.listeners

import khusainov.farrukh.communityapp.data.auth.remote.SignInRequest
import khusainov.farrukh.communityapp.data.posts.remote.Post

/**
 *Created by farrukh_kh on 4/25/21 12:29 PM
 *khusainov.farrukh.communityapp.utils.clicklisteners
 **/

//interface for navigating between fragments (must be replaced with Navigation Component)
interface HomeActivityListener {
    //fun to save user's email and password
    fun saveSignInData(value: SignInRequest)

    //fun to get user's email and password
    fun getSignInData(): SignInRequest?

    //fun to save user's ID
    fun saveUserId(userId: String)

    //fun to get user's ID
    fun getUserId(): String

    //fun to make intent for sharing article
    fun shareIntent(article: Post)
}