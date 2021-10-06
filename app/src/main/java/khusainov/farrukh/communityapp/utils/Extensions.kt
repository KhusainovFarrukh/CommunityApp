package khusainov.farrukh.communityapp.utils

import android.text.Html
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.posts.remote.Post
import khusainov.farrukh.communityapp.databinding.ViewholderCommentBinding
import khusainov.farrukh.communityapp.utils.Constants.VALUE_DEFAULT
import khusainov.farrukh.communityapp.utils.listeners.CommentClickListener

/**
 *Created by FarrukhKhusainov on 4/22/21 1:08 PM
 **/
//extension fun for setting Comment data to Views
fun ViewholderCommentBinding.setCommentToViews(
	comment: Post,
	commentClickListener: CommentClickListener,
) {
	//set TextViews
	txvName.text = comment.user?.profile?.name ?: root.context.getString(R.string.unknown_author)
	txvTime.text = comment.getDifference()
	txvComment.text = Html.fromHtml(comment.content).trim()
	txvLike.text = comment.stats.likes.toString()
	txvReply.text = comment.stats.comments.toString()

	//load profile photo
	imvProfile.load(comment.user?.profile?.photo) {
		crossfade(true)
		placeholder(R.drawable.ic_account_circle)
		transformations(CircleCropTransformation())
	}

	//set ClickListeners
	imvProfile.setOnClickListener {
		commentClickListener.onCommentAuthorClick(comment.user?.id ?: VALUE_DEFAULT)
	}
	txvName.setOnClickListener {
		commentClickListener.onCommentAuthorClick(comment.user?.id ?: VALUE_DEFAULT)
	}
	txvLike.setOnClickListener {
		commentClickListener.onLikeCommentClick(comment)
	}
	txvReply.setOnClickListener {
		etComment.isVisible = true
		txvSendComment.isVisible = true
	}
	txvMore.setOnClickListener {
		val popUp = PopupMenu(root.context, txvMore)
		if (comment.user?.id == commentClickListener.getUserId()) {
			popUp.inflate(R.menu.popup_menu_author)
		} else {
			popUp.inflate((R.menu.popup_menu_not_author))
		}

		popUp.setOnMenuItemClickListener {
			when (it.itemId) {
				R.id.item_report -> {
					commentClickListener.onReportClick(comment.id)
				}
				R.id.item_delete -> {
					commentClickListener.onDeleteCommentClick(comment.id)
				}
			}
			true
		}
		popUp.show()
	}

	//set isLiked state to ImageView
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
}

fun View.comingSoon() {
	setOnClickListener {
		Toast.makeText(this.context, "Coming soon...", Toast.LENGTH_SHORT).show()
	}
}