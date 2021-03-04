package khusainov.farrukh.communityapp.utils.clicklisteners

import khusainov.farrukh.communityapp.data.model.Notification

interface NotificationClickListener {
    fun onNotificationClick(notification: Notification)
}