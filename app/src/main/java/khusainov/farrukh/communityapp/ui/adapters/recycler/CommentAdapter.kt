package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.listeners.CommentClickListener
import khusainov.farrukh.communityapp.utils.setCommentToViews
import khusainov.farrukh.communityapp.utils.itemcallbacks.CommentItemCallback

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/
//PagingDataAdapter for comments of article
class CommentAdapter(private val commentClickListener: CommentClickListener) :
	PagingDataAdapter<Post, CommentAdapter.CommentViewHolder>(CommentItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
		ViewholderCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
		holder.onBind(getItem(position)!!)
	}

	inner class CommentViewHolder(private val binding: ViewholderCommentBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBind(comment: Post) = with(binding) {
			//set comment to views
			setCommentToViews(comment, commentClickListener)

			//set ClickListener for sending new response
			txvSendComment.setOnClickListener {
				if (etComment.text.isNotEmpty()) {
					commentClickListener.onReplyClick(etComment.text.toString(),
						comment.id)
					etComment.text.clear()
					etComment.isVisible = false
					txvSendComment.isVisible = false
				} else {
					Toast.makeText(
						root.context,
						root.context.getString(R.string.empty_text),
						Toast.LENGTH_SHORT
					).show()
				}
			}

			//set adapter for responses of current comment
			val adapter = SubCommentAdapter(commentClickListener, snapshot().items)
			rvResponses.adapter = adapter

			//if comment has body of its replies, set them to RecyclerView
			if (comment.onlyResponsesId()) {
				if (comment.responses.size() > 0) {
					txvAnotherComments.text =
						root.context.getString(R.string.other_comments, comment.responses.size())
					txvAnotherComments.isVisible = true
				} else {
					txvAnotherComments.isVisible = false
				}
			} else {
				//temporary try-catch
				try {
					txvAnotherComments.isVisible = false
					val list = mutableListOf<Post>()
					comment.responses.forEach {
						list.add(Gson().fromJson(it, Post::class.java))
					}
					adapter.submitList(list)
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
	}
}

//ListAdapter for responses (sub-comment) of comment
class SubCommentAdapter(
	private val commentClickListener: CommentClickListener,
	private val parentComments: List<Post>,
) : ListAdapter<Post, SubCommentAdapter.CommentViewHolder>(CommentItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
		ViewholderCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
	)

	override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
		holder.onBind(getItem(position))
	}

	inner class CommentViewHolder(private val binding: ViewholderCommentBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBind(comment: Post) = with(binding) {
			//set comment to views
			setCommentToViews(comment, commentClickListener)

			//set ClickListener for sending new response
			txvSendComment.setOnClickListener {
				if (etComment.text.isNotEmpty()) {
					parentComments.forEach {
						if (comment.replyTo.asString == it.id) {
							commentClickListener.onReplyClick(
								etComment.text.toString(),
								it.id
							)
							return@forEach
						}
					}
					etComment.text.clear()
					etComment.isVisible = false
					txvSendComment.isVisible = false
				} else {
					Toast.makeText(
						root.context,
						root.context.getString(R.string.empty_text),
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}
}