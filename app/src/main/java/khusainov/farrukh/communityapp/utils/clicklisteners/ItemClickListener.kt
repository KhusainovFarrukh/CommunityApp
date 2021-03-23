package khusainov.farrukh.communityapp.utils.clicklisteners

import android.widget.Toast
import khusainov.farrukh.communityapp.data.model.Notification
import khusainov.farrukh.communityapp.ui.activities.HomeActivity
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by FarrukhKhusainov on 3/23/21 11:23 PM
 **/
class ItemClickListener(private val activityListener: HomeActivityListener?) {
    fun onArticleClick(articleId: String) {
        activityListener?.showArticleDetailsFragment(articleId)
    }

    fun onNotificationClick(notification: Notification) {
        when (notification.verb) {
            Constants.KEY_NOTIFICATION_POST -> {
                activityListener?.showArticleDetailsFragment(notification.objects[0].articleId)
            }
            Constants.KEY_NOTIFICATION_POST_UPVOTE -> {
                activityListener?.showUserFragment(notification.from[0].userId)
            }
            Constants.KEY_NOTIFICATION_REPLY -> {
                //TODO replace to show comment on article details screen
                activityListener?.showArticleDetailsFragment(notification.objects[0].parent.articleId)
            }
            Constants.KEY_NOTIFICATION_FOLLOW_USER -> {
                activityListener?.showUserFragment(notification.from[0].userId)
            }
            else -> {
                Toast.makeText(
                    (activityListener as HomeActivity),
                    "${notification.verb} turidagi noma`lum xabarnoma. Dastur muallifiga xabar bering",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onTopicClick(topicId: String) {
        activityListener?.showTopicFragment(topicId)
    }

}