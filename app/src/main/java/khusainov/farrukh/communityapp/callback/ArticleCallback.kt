package khusainov.farrukh.communityapp.callback

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.model.Article

class ArticleCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}