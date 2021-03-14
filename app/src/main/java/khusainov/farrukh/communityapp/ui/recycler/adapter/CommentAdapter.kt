package khusainov.farrukh.communityapp.ui.recycler.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.model.ArticleDetailsWithResponses
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.clicklisteners.CommentClickInterface

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/

//TODO remove it
@Suppress("DEPRECATION")
class CommentAdapter(private val commentClickInterface: CommentClickInterface) :
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
                txvComment.text = Html.fromHtml(comment.content).trim()
                txvLike.text = comment.stats.likesCount.toString()
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

                if (comment.isLiked) {
                    txvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite, 0, 0, 0)
                    txvLike.setOnClickListener {
                        commentClickInterface.onLikeCommentClick(comment.articleId, true)
                    }
                } else {
                    txvLike.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite_border,
                        0,
                        0,
                        0
                    )
                    txvLike.setOnClickListener {
                        commentClickInterface.onLikeCommentClick(comment.articleId, false)
                    }
                }
                val adapter = CommentAdapter(commentClickInterface)
                rvResponses.adapter = adapter
                adapter.submitList(comment.responses)
            }
        }
    }
}