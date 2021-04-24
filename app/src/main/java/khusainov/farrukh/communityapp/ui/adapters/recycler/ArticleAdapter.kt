package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderArticleBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener

class ArticleAdapter(private val articleClickListener: ItemClickListener) :
    PagingDataAdapter<Post, ArticleViewHolder>(object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ViewholderArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            articleClickListener.onArticleClick(getItem(position)!!.id)
        }
        holder.onBindArticle(getItem(position)!!)
    }
}

class ArticleViewHolder(private val binding: ViewholderArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBindArticle(article: Post) {
        binding.apply {
            txvTitle.text = article.title?.trim()
            txvAuthor.text = article.user?.profile?.name ?: "Unknown"
            txvViews.text = article.stats.viewsCount.toString()
            txvLikes.text = article.stats.likes.toString()
            txvComments.text = article.stats.comments.toString()
            if (article.imagesList.isNotEmpty()) {
                imvImage.load(article.imagesList[0].imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
//                    kotlin.error(R.drawable.no_image)
                }
            } else {
                imvImage.load(ContextCompat.getDrawable(itemView.context, R.drawable.no_image))
            }
        }
    }
}