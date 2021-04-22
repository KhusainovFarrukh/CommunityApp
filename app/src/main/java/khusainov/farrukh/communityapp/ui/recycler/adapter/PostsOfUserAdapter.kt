package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderArticleOfUserBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener

class PostsOfUserAdapter(
    private val clickListener: ItemClickListener,
) : PagingDataAdapter<Post, PostsOfUserAdapter.ArticleInUserViewHolder>(object :
        DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleInUserViewHolder {
        return ArticleInUserViewHolder(
            ViewholderArticleOfUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleInUserViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener.onArticleClick(getItem(position)!!.id)
        }
        holder.onBindArticle(getItem(position)!!)
    }

    inner class ArticleInUserViewHolder(private val binding: ViewholderArticleOfUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindArticle(article: Post) {
            binding.apply {
                if (article.title.isNullOrEmpty()) {
                    if (article.onlyParentId()) {
                        txvTitle.text = "Komment ga javoban:"
                    } else {
                        Gson().fromJson(article.parent, Post::class.java).let {
                            txvTitle.text = "\"${it.title ?: "Komment"}\" ga javoban:"
                        }
                    }
                } else {
                    txvTitle.text = article.title.trim()
                }
                txvViews.text = article.stats.viewsCount.toString()
                txvLikes.text = article.stats.likes.toString()
                txvComments.text = article.stats.comments.toString()
                txvSummary.text = Html.fromHtml(article.summary)
                val hashtagAdapter = HashTagAdapter(clickListener)
                rvHashtags.adapter = hashtagAdapter
                hashtagAdapter.submitList(article.topics)
                txvTime.text = article.getDifference()
            }
        }
    }
}