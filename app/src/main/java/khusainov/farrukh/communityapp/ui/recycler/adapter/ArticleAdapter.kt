package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.ui.recycler.callback.ArticleCallback
import khusainov.farrukh.communityapp.utils.clicklisteners.ArticleClickListener
import khusainov.farrukh.communityapp.data.model.Article

class ArticleAdapter(private val articleClickListener: ArticleClickListener) :
    ListAdapter<Article, ArticleViewHolder>(ArticleCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            articleClickListener.onArticleClick(getItem(position))
        }
        holder.onBindArticle(getItem(position))
    }
}

class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val txvTitle = itemView.findViewById<TextView>(R.id.txv_title)
    private val txvAuthor = itemView.findViewById<TextView>(R.id.txv_author)
    private val txvViews = itemView.findViewById<TextView>(R.id.txv_views)
    private val txvLikes = itemView.findViewById<TextView>(R.id.txv_likes)
    private val txvComments = itemView.findViewById<TextView>(R.id.txv_comments)
    private val imvImage = itemView.findViewById<ImageView>(R.id.imv_image)

    fun onBindArticle(article: Article) {
        txvTitle.text = article.title?.trim()
        txvAuthor.text = article.user?.profile?.name ?: "Noma`lum"
        txvViews.text = article.stats.viewsCount.toString()
        txvLikes.text = article.stats.likesCount.toString()
        txvComments.text = article.stats.commentsCount.toString()
        if (article.imagesList.isNotEmpty()) {
            imvImage.load(article.imagesList[0].imageLink) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.no_image)
            }
        } else {
            imvImage.load(ContextCompat.getDrawable(itemView.context, R.drawable.no_image))
        }
    }
}