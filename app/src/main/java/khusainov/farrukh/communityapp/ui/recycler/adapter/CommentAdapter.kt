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
import khusainov.farrukh.communityapp.data.model.ArticleDetails
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding

/**
 *Created by FarrukhKhusainov on 3/8/21 9:59 PM
 **/
class CommentAdapter : ListAdapter<ArticleDetails, CommentViewHolder>(object :
    DiffUtil.ItemCallback<ArticleDetails>() {
    override fun areItemsTheSame(oldItem: ArticleDetails, newItem: ArticleDetails) =
        oldItem.articleId == newItem.articleId

    override fun areContentsTheSame(oldItem: ArticleDetails, newItem: ArticleDetails) =
        oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
        ViewholderCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class CommentViewHolder(private val binding: ViewholderCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(comment: ArticleDetails) {
        binding.apply {
            txvName.text = comment.user?.profile?.name ?: "Unknown"
            txvComment.text = Html.fromHtml(comment.content).trim()
            txvLike.text = comment.stats.likesCount.toString()
            imvProfile.load(comment.user?.profile?.profilePhoto) {
                crossfade(true)
                placeholder(R.drawable.ic_account_circle)
                transformations(CircleCropTransformation())
            }
        }
    }
}