package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.ViewholderTopicBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener

class TopicAdapter(private val topicClickListener: ItemClickListener) :
    ListAdapter<Topic, TopicAdapter.TopicViewHolder>(object : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic) =
            oldItem == newItem
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            ViewholderTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
//            articleClickListener.onArticleClick(getItem(position))
        }
        holder.onBindTopic(getItem(position))
    }

    inner class TopicViewHolder(private val binding: ViewholderTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindTopic(topic: Topic) {
            binding.apply {
                root.setOnClickListener {
                    topicClickListener.onTopicClick(topic.id)
                }
                txvTitle.text = topic.name
                txvPosts.text = topic.stats.posts.toString()
                imvIcon.load(topic.picture) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
//                kotlin.error(R.drawable.no_image)
                }
            }
        }
    }
}
