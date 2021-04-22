package khusainov.farrukh.communityapp.utils.itemcallbacks

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.models.Post

/**
 *Created by FarrukhKhusainov on 4/22/21 1:06 PM
 **/
class PostItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun getChangePayload(
        oldItem: Post,
        newItem: Post,
    ) = false

    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post,
    ) = oldItem == newItem
}