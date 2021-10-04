package khusainov.farrukh.communityapp.utils.diff_utils

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by farrukh_kh on 4/25/21 11:53 PM
 *khusainov.farrukh.communityapp.utils.itemcallbacks
 **/
class ArticleItemCallback : DiffUtil.ItemCallback<Post>() {
	override fun areItemsTheSame(oldItem: Post, newItem: Post) =
		oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Post, newItem: Post) =
		oldItem == newItem
}