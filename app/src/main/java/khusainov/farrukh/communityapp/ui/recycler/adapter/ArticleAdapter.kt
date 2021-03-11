package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.model.Article
import khusainov.farrukh.communityapp.databinding.ViewholderArticleBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.ArticleClickListener

class ArticleAdapter(private val articleClickListener: ArticleClickListener) :
    ListAdapter<Article, ArticleViewHolder>(object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.articleId == newItem.articleId

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
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
            articleClickListener.onArticleClick(getItem(position).articleId)
        }
        holder.onBindArticle(getItem(position))
    }
}

class ArticleViewHolder(private val binding: ViewholderArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBindArticle(article: Article) {
        binding.apply {
            txvTitle.text = article.title?.trim()
            txvAuthor.text = article.user?.profile?.name ?: "Noma`lum"
            txvViews.text = article.stats.viewsCount.toString()
            txvLikes.text = article.stats.likesCount.toString()
            txvComments.text = article.stats.commentsCount.toString()
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