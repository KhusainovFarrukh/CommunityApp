package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderArticleOfUserBinding
import khusainov.farrukh.communityapp.utils.Constants.KEY_RESPONSE
import khusainov.farrukh.communityapp.utils.itemcallbacks.ArticleItemCallback

class PostsOfUserAdapter(
	private val topicClick: (String) -> Unit,
	private val postClick: (String) -> Unit,
) : PagingDataAdapter<Post, PostsOfUserAdapter.ArticleInUserViewHolder>(ArticleItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleInUserViewHolder(
		ViewholderArticleOfUserBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
	)

	override fun onBindViewHolder(holder: ArticleInUserViewHolder, position: Int) {
		holder.onBindArticle(getItem(position)!!)
	}

	inner class ArticleInUserViewHolder(private val binding: ViewholderArticleOfUserBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBindArticle(article: Post) = with(binding) {
			root.setOnClickListener {
				if (article.type == KEY_RESPONSE) {
					if (article.onlyParentId()) {
						postClick.invoke(article.parent.asString)
					} else {
						try {
							postClick.invoke(Gson().fromJson(article.parent,
								Post::class.java).id)
						} catch (e: Exception) {
							e.printStackTrace()
						}
					}
				} else {
					postClick.invoke(article.id)
				}
			}

			//TODO temporary code (handle replyTo)
			//set title of itemView depending on if it is article or comment
			if (article.type == KEY_RESPONSE) {
				if (article.onlyParentId()) {
					txvTitle.text = root.context.getString(R.string.reply_to_comment)
				} else {
					Gson().fromJson(article.parent, Post::class.java).let {
						txvTitle.text = root.context.getString(R.string.reply_to,
							it.title?.trim()
								?: root.context.getString(R.string.reply_to_comment))
					}
				}
			} else {
				txvTitle.text =
					article.title?.trim() ?: root.context.getString(R.string.reply_to_comment)
			}

			txvViews.text = article.stats.viewsCount.toString()
			txvLikes.text = article.stats.likes.toString()
			txvComments.text = article.stats.comments.toString()
			txvSummary.text = Html.fromHtml(article.summary)
			txvTime.text = article.getDifference()

			val topicOfArticleAdapter = TopicOfArticleAdapter { topicId ->
				topicClick.invoke(topicId)
			}
			rvHashtags.adapter = topicOfArticleAdapter
			topicOfArticleAdapter.submitList(article.topics)
		}
	}
}