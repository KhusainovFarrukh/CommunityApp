package khusainov.farrukh.communityapp.ui.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.model.Notification

class NotificationCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}