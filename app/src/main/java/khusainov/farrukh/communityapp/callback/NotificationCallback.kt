package khusainov.farrukh.communityapp.callback

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.model.Notification

class NotificationCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}