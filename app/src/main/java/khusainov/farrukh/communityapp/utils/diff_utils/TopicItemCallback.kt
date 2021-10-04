package khusainov.farrukh.communityapp.utils.diff_utils

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.models.Topic

/**
 *Created by farrukh_kh on 4/26/21 11:22 AM
 *khusainov.farrukh.communityapp.utils.itemcallbacks
 **/
class TopicItemCallback : DiffUtil.ItemCallback<Topic>() {
	override fun areItemsTheSame(oldItem: Topic, newItem: Topic) =
		oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Topic, newItem: Topic) = oldItem == newItem
}