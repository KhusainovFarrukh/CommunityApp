package khusainov.farrukh.communityapp.ui.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import khusainov.farrukh.communityapp.data.model.Article

class ArticleCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}