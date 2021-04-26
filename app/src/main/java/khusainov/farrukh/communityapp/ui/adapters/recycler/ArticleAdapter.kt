package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderArticleBinding
import khusainov.farrukh.communityapp.utils.Constants.VALUE_UNKNOWN
import khusainov.farrukh.communityapp.utils.itemcallbacks.ArticleItemCallback

class ArticleAdapter(private val itemClick: (String) -> Unit) :
	PagingDataAdapter<Post, ArticleAdapter.ArticleViewHolder>(ArticleItemCallback()) {

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
		holder.onBindArticle(getItem(position)!!)
	}

	inner class ArticleViewHolder(private val binding: ViewholderArticleBinding) :
		RecyclerView.ViewHolder(binding.root) {

		init {
			binding.root.setOnClickListener {
				itemClick.invoke(getItem(absoluteAdapterPosition)!!.id)
			}
		}

		fun onBindArticle(article: Post) = with(binding) {
			txvTitle.text = article.title?.trim()
			txvAuthor.text = article.user?.profile?.name?.trim() ?: VALUE_UNKNOWN
			txvViews.text = article.stats.viewsCount.toString()
			txvLikes.text = article.stats.likes.toString()
			txvComments.text = article.stats.comments.toString()

			if (article.imagesList.isNotEmpty()) {
				imvImage.load(article.imagesList[0].imageLink) {
					crossfade(true)
					placeholder(R.drawable.ic_launcher_foreground)
				}
			} else {
				imvImage.load(ContextCompat.getDrawable(itemView.context, R.drawable.no_image))
			}
		}
	}
}