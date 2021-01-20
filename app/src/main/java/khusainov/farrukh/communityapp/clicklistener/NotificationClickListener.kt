package khusainov.farrukh.communityapp.clicklistener

import khusainov.farrukh.communityapp.model.Notification

interface NotificationClickListener {
    fun onNotificationClick(notification: Notification)
}