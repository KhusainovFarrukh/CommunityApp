package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickListener

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/

//TODO remove it
@Suppress("DEPRECATION")
open class CommentAdapter(private val commentClickListener: CommentClickListener) :
    ListAdapter<Post, CommentAdapter.CommentViewHolder>(object :
        DiffUtil.ItemCallback<Post>() {
        override fun getChangePayload(
            oldItem: Post,
            newItem: Post,
        ) = false

        override fun areItemsTheSame(
            oldItem: Post,
            newItem: Post,
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post,
        ) =
            oldItem == newItem
    }) {

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
                txvName.text = comment.user?.profile?.name ?: "Unknown"
                txvTime.text = comment.getDifference()
                txvComment.text = Html.fromHtml(comment.content).trim()
                txvLike.text = comment.stats.likes.toString()
                txvReply.text = comment.stats.comments.toString()
                imvProfile.load(comment.user?.profile?.photo) {
                    crossfade(true)
                    placeholder(R.drawable.ic_account_circle)
                    transformations(CircleCropTransformation())
                }
                imvProfile.setOnClickListener {
                    commentClickListener.onCommentAuthorClick(comment.user?.id ?: "")
                }
                txvName.setOnClickListener {
                    commentClickListener.onCommentAuthorClick(comment.user?.id ?: "")
                }
                txvLike.setOnClickListener {
                    onLikeClick(comment)
                }
                txvReply.setOnClickListener {
                    etComment.isVisible = true
                    txvSendComment.isVisible = true
                }
                txvSendComment.setOnClickListener {
                    if (etComment.text.isNotEmpty()) {
                        commentClickListener.onWriteSubCommentClick(
                            etComment.text.toString(),
                            comment
                        )
                        etComment.text.clear()
                        etComment.isVisible = false
                        txvSendComment.isVisible = false
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "Type your response!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                txvMore.setOnClickListener {
                    val popUp = PopupMenu(itemView.context, txvMore)
                    if (comment.user?.id == commentClickListener.getUserId()) {
                        popUp.inflate(R.menu.popup_menu_author)
                    } else {
                        popUp.inflate((R.menu.popup_menu_not_author))
                    }

                    popUp.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_report -> {
                                commentClickListener.showReportDialog(comment.id)
                            }
                            R.id.item_delete -> {
                                onDeleteClick(comment.id)
                            }
                        }
                        true
                    }

                    popUp.show()
                }

                if (comment.isLiked) {
                    txvLike.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite,
                        0,
                        0,
                        0
                    )
                } else {
                    txvLike.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite_border,
                        0,
                        0,
                        0
                    )
                }

                val adapter = SubCommentAdapter(commentClickListener)
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
                    val list: MutableList<Post> = mutableListOf()
                    comment.responses.forEach {
                        list.add(Gson().fromJson(it, Post::class.java))
                    }
                    adapter.submitList(list)
                }
            }
        }
    }

    open fun onLikeClick(comment: Post) {
        commentClickListener.onLikeCommentClick(comment.id, comment.isLiked)
    }

    open fun onDeleteClick(commentId: String) {
        commentClickListener.onDeleteCommentClick(commentId)
    }
}

class SubCommentAdapter(private val commentClickListener: CommentClickListener) :
    CommentAdapter(commentClickListener) {
    override fun onLikeClick(comment: Post) {
        commentClickListener.onLikeSubCommentClick(comment.id, comment.isLiked)
    }

    override fun onDeleteClick(commentId: String) {
        commentClickListener.onDeleteSubCommentClick(commentId)
    }
}