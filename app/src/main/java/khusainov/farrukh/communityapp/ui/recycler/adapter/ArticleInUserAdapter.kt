package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.databinding.ViewholderArticleOfUserBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ArticleClickListener
import khusainov.farrukh.communityapp.utils.clicklisteners.TopicClickListener

class ArticleInUserAdapter(
    private val articleClickListener: ArticleClickListener,
    private val topicClickListener: TopicClickListener,
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
            articleClickListener.onArticleClick(getItem(position).articleId)
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
                txvHashtags.text = article.getHashtags()
                txvHashtags.setOnClickListener {
                    //TODO now getting only first topic
                    topicClickListener.onTopicClick(article.topics[0].topicId)
                }
                txvTime.text = article.getDifference()
            }
        }
    }
}