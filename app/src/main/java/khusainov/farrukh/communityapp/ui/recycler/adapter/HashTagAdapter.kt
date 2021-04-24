package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.ViewholderHashtagBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener
import java.util.*

/**
 *Created by FarrukhKhusainov on 3/23/21 4:11 PM
 **/
class HashTagAdapter(
    private val topicClickListener: ItemClickListener,
) : ListAdapter<Topic, HashTagAdapter.HashtagViewHolder>(object : DiffUtil.ItemCallback<Topic>() {
    override fun areItemsTheSame(oldItem: Topic, newItem: Topic) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Topic, newItem: Topic) = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HashtagViewHolder(
        ViewholderHashtagBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(holder: HashtagViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class HashtagViewHolder(private val binding: ViewholderHashtagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(topic: Topic) {
            binding.apply {
                txvHashtag.text = "#${topic.name.trim().toLowerCase(Locale.ROOT).replace(" ", "_")}"
                txvHashtag.setOnClickListener {
                    topicClickListener.onTopicClick(topic.id)
                }
            }
        }
    }
}