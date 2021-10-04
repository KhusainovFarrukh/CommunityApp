package khusainov.farrukh.communityapp.ui.topic_details.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.ViewholderTopicBinding
import khusainov.farrukh.communityapp.utils.diff_utils.TopicItemCallback

class TopicAdapter(private val itemClick: (String) -> Unit) :
	ListAdapter<Topic, TopicAdapter.TopicViewHolder>(TopicItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TopicViewHolder(
		ViewholderTopicBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
	)

	override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
		holder.onBindTopic(getItem(position))
	}

	inner class TopicViewHolder(private val binding: ViewholderTopicBinding) :
		RecyclerView.ViewHolder(binding.root) {

		init {
			binding.root.setOnClickListener {
				itemClick.invoke(getItem(absoluteAdapterPosition)!!.id)
			}
		}

		fun onBindTopic(topic: Topic) = with(binding) {

			root.startAnimation(AnimationUtils.loadAnimation(root.context, R.anim.enlarge_with_alpha))

			txvTitle.text = topic.name
			txvPosts.text = topic.stats.posts.toString()
			imvIcon.load(topic.picture) {
				crossfade(true)
				placeholder(R.drawable.ic_launcher_foreground)
			}
		}
	}
}
