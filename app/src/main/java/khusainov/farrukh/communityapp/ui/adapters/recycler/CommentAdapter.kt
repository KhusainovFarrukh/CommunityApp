package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickListener
import khusainov.farrukh.communityapp.utils.extensions.setCommentToViews
import khusainov.farrukh.communityapp.utils.itemcallbacks.PostItemCallback

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/
//TODO remove it
@Suppress("DEPRECATION")
class CommentAdapter(private val commentClickListener: CommentClickListener) :
	PagingDataAdapter<Post, CommentAdapter.CommentViewHolder>(PostItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
        ViewholderCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

	override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
		holder.onBind(getItem(position)!!)
	}

	inner class CommentViewHolder(private val binding: ViewholderCommentBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun onBind(comment: Post) {
			binding.apply {
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
                            "Type your response!",
                            Toast.LENGTH_SHORT
                        ).show()
					}
				}

				//set adapter for responses of current comment
				val adapter = SubCommentAdapter(commentClickListener, snapshot().items)
				rvResponses.adapter = adapter

				if (comment.onlyResponsesId()) {
					if (comment.responses.size() > 0) {
						txvAnotherComments.text =
							"There is another ${comment.responses.size()} comments..."
						txvAnotherComments.isVisible = true
					} else {
						txvAnotherComments.isVisible = false
					}
				} else {
					txvAnotherComments.isVisible = false
					val list = mutableListOf<Post>()
					comment.responses.forEach {
						list.add(Gson().fromJson(it, Post::class.java))
					}
					adapter.submitList(list)
				}
			}
		}
	}
}

class SubCommentAdapter(
    private val commentClickListener: CommentClickListener,
    private val parentComments: List<Post>,
) : ListAdapter<Post, SubCommentAdapter.CommentViewHolder>(PostItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
        ViewholderCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

	override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
		holder.onBind(getItem(position))
	}

	inner class CommentViewHolder(private val binding: ViewholderCommentBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun onBind(comment: Post) {
			binding.apply {
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
                            "Type your response!",
                            Toast.LENGTH_SHORT
                        ).show()
					}
				}
			}
		}
	}
}