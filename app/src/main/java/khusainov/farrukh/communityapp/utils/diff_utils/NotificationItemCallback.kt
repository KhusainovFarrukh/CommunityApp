package khusainov.farrukh.communityapp.utils.diff_utils

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.notifications.remote.Notification

/**
 *Created by farrukh_kh on 4/26/21 4:07 PM
 *khusainov.farrukh.communityapp.utils.itemcallbacks
 **/
class NotificationItemCallback :
	DiffUtil.ItemCallback<Notification>() {
	override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
		oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
		oldItem == newItem
}