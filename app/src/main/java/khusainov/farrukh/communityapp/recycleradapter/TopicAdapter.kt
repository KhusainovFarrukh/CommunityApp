package khusainov.farrukh.communityapp.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.callback.TopicCallback
import khusainov.farrukh.communityapp.model.Topic

class TopicAdapter : ListAdapter<Topic, TopicViewHolder>(TopicCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_topic, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
//            articleClickListener.onArticleClick(getItem(position))
        }
        holder.onBindArticle(getItem(position))
    }
}

class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val txvTitle = itemView.findViewById<TextView>(R.id.txv_title)
    private val txvPosts = itemView.findViewById<TextView>(R.id.txv_posts)
    private val imvIcon = itemView.findViewById<ImageView>(R.id.imv_icon)

    fun onBindArticle(topic: Topic) {
        txvTitle.text = topic.name
        txvPosts.text = topic.stats.posts.toString()
        imvIcon.load(topic.picture) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.no_image)
        }
    }
}
