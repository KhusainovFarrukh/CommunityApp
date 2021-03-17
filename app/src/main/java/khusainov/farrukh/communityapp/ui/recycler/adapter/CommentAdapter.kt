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
import khusainov.farrukh.communityapp.data.model.ArticleDetailsWithResponses
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickInterface

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/

//TODO remove it
@Suppress("DEPRECATION")
open class CommentAdapter(private val commentClickInterface: CommentClickInterface) :
    ListAdapter<ArticleDetailsWithResponses, CommentAdapter.CommentViewHolder>(object :
        DiffUtil.ItemCallback<ArticleDetailsWithResponses>() {
        override fun getChangePayload(
            oldItem: ArticleDetailsWithResponses,
            newItem: ArticleDetailsWithResponses
        ) = false

        override fun areItemsTheSame(
            oldItem: ArticleDetailsWithResponses,
            newItem: ArticleDetailsWithResponses
        ) =
            oldItem.articleId == newItem.articleId

        override fun areContentsTheSame(
            oldItem: ArticleDetailsWithResponses,
            newItem: ArticleDetailsWithResponses
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
        fun onBind(comment: ArticleDetailsWithResponses) {
            binding.apply {
                txvName.text = comment.user?.profile?.name ?: "Unknown"
                txvTime.text = comment.getDifference()
                txvComment.text = Html.fromHtml(comment.content).trim()
                txvLike.text = comment.stats.likesCount.toString()
                txvReply.text = comment.stats.commentsCount.toString()
                imvProfile.load(comment.user?.profile?.profilePhoto) {
                    crossfade(true)
                    placeholder(R.drawable.ic_account_circle)
                    transformations(CircleCropTransformation())
                }
                imvProfile.setOnClickListener {
                    commentClickInterface.onCommentAuthorClick(comment.user?.userId ?: "")
                }
                txvName.setOnClickListener {
                    commentClickInterface.onCommentAuthorClick(comment.user?.userId ?: "")
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
                        commentClickInterface.onWriteSubCommentClick(
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
                    if (comment.user?.userId == commentClickInterface.getUserId()) {
                        popUp.inflate(R.menu.popup_menu_author)
                    } else {
                        popUp.inflate((R.menu.popup_menu_not_author))
                    }

                    popUp.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_report -> {
                                commentClickInterface.showReportDialog(comment.articleId)
                            }
                            R.id.item_delete -> {
                                onDeleteClick(comment.articleId)
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

                val adapter = SubCommentAdapter(commentClickInterface)
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
                    val list: MutableList<ArticleDetailsWithResponses> = mutableListOf()
                    comment.responses.forEach {
                        list.add(Gson().fromJson(it, ArticleDetailsWithResponses::class.java))
                    }
                    adapter.submitList(list)
                }
            }
        }
    }

    open fun onLikeClick(comment: ArticleDetailsWithResponses) {
        commentClickInterface.onLikeCommentClick(comment.articleId, comment.isLiked)
    }

    open fun onDeleteClick(commentId: String) {
        commentClickInterface.onDeleteCommentClick(commentId)
    }
}

class SubCommentAdapter(private val commentClickInterface: CommentClickInterface) :
    CommentAdapter(commentClickInterface) {
    override fun onLikeClick(comment: ArticleDetailsWithResponses) {
        commentClickInterface.onLikeSubCommentClick(comment.articleId, comment.isLiked)
    }

    override fun onDeleteClick(commentId: String) {
        commentClickInterface.onDeleteSubCommentClick(commentId)
    }
}