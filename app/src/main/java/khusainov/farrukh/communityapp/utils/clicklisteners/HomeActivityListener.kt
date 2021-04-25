package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.models.SignInData

/**
 *Created by farrukh_kh on 4/25/21 12:29 PM
 *khusainov.farrukh.communityapp.utils.clicklisteners
 **/

//interface for navigating between fragments (must be replaced with Navigation Component)
interface HomeActivityListener {
    //fun to show MainFragment
    fun showMainFragment()

    //fun to show Sign in form
    fun showLoginDialog()

    //fun to show report a post form
    fun showReportDialog(articleId: String)

    //fun to show notification
    fun showNotificationsFragment()

    //fun to show single article
    fun showArticleDetailsFragment(articleId: String)

    //fun to save user's email and password
    fun saveSignInData(value: SignInData)

    //fun to get user's email and password
    fun getSignInData(): SignInData?

    //fun to save user's ID
    fun saveUserId(userId: String)

    //fun to get user's ID
    fun getUserId(): String

    //fun to show single user
    fun showUserFragment(userId: String)

    //fun to make intent for sharing article
    fun shareIntent(article: Post)

    //fun to show single topic
    fun showTopicFragment(topicId: String)
}