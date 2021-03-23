package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.databinding.ViewholderArticleOfUserBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener

class ArticleInUserAdapter(
    private val clickListener: ItemClickListener,
) :
    ListAdapter<Article, ArticleInUserAdapter.ArticleInUserViewHolder>(object :
        DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.articleId == newItem.articleId

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
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
            clickListener.onArticleClick(getItem(position).articleId)
        }
        holder.onBindArticle(getItem(position))
    }

    inner class ArticleInUserViewHolder(private val binding: ViewholderArticleOfUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindArticle(article: Article) {
            binding.apply {
                if (article.title.isNullOrEmpty()) {
                    txvTitle.text = "\"${article.parent.title ?: "Komment"}\" ga javoban:"
                } else {
                    txvTitle.text = article.title.trim()
                }
                txvViews.text = article.stats.viewsCount.toString()
                txvLikes.text = article.stats.likesCount.toString()
                txvComments.text = article.stats.commentsCount.toString()
                txvSummary.text = Html.fromHtml(article.summary)
                val hashtagAdapter = HashtagAdapter(clickListener)
                rvHashtags.adapter = hashtagAdapter
                hashtagAdapter.submitList(article.topics)
                txvTime.text = article.getDifference()
            }
        }
    }
}