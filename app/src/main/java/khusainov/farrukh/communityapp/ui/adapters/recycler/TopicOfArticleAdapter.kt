package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Topic
import khusainov.farrukh.communityapp.databinding.ViewholderHashtagBinding
import khusainov.farrukh.communityapp.utils.itemcallbacks.TopicItemCallback
import java.util.*

/**
 *Created by FarrukhKhusainov on 3/23/21 4:11 PM
 **/
class TopicOfArticleAdapter(private val itemClick: (String) -> Unit) :
	ListAdapter<Topic, TopicOfArticleAdapter.TopicOfArticleViewHolder>(TopicItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TopicOfArticleViewHolder(
		ViewholderHashtagBinding.inflate(LayoutInflater.from(parent.context))
	)

	override fun onBindViewHolder(holder: TopicOfArticleViewHolder, position: Int) {
		holder.onBind(getItem(position))
	}

	inner class TopicOfArticleViewHolder(private val binding: ViewholderHashtagBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBind(topic: Topic) = with(binding) {
			txvHashtag.text = root.context.getString(R.string.hashtag,
				topic.name.toLowerCase(Locale.ROOT).replace(" ", "_").trim())
			txvHashtag.setOnClickListener {
				itemClick.invoke(topic.id)
			}
		}
	}
}