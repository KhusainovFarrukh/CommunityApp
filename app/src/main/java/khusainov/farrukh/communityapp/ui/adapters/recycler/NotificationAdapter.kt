package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Notification
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.databinding.ViewholderNotificationBinding
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_FOLLOW_USER
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_POST
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_POST_UPVOTE
import khusainov.farrukh.communityapp.utils.Constants.KEY_NOTIFICATION_REPLY
import khusainov.farrukh.communityapp.utils.itemcallbacks.NotificationItemCallback

class NotificationAdapter(private val itemClick: (Notification) -> Unit) :
	PagingDataAdapter<Notification, NotificationAdapter.NotificationViewHolder>(
		NotificationItemCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotificationViewHolder(
		ViewholderNotificationBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
	)

	override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
		holder.onBindArticle(getItem(position)!!)
	}

	inner class NotificationViewHolder(private val binding: ViewholderNotificationBinding) :
		RecyclerView.ViewHolder(binding.root) {

		init {
			binding.root.setOnClickListener {
				itemClick.invoke(getItem(absoluteAdapterPosition)!!)
			}
		}

		fun onBindArticle(notification: Notification) = with(binding) {
			if (notification.isRead) {
				txvNotificationText.typeface = Typeface.DEFAULT
			} else {
				txvNotificationText.typeface = Typeface.DEFAULT_BOLD
			}

			//TODO needs code optimization
			//notification types: create post, like post, reply (or add comment), follow user
			when (notification.verb) {

				KEY_NOTIFICATION_POST -> {
					val createdPostTitle = if (notification.objects.isNotEmpty()) {
						notification.objects[0].title?.trim()
					} else {
						root.context.getString(R.string.unknown_article)
					}
					txvNotificationText.text = itemView.context.getString(
						R.string.notification_post,
						if (notification.from.isNotEmpty()) {
							notification.from[0].profile.name.trim()
						} else {
							root.context.getString(
								R.string.unknown_author)
						},
						createdPostTitle)

					imvIcon.setImageDrawable(
						ContextCompat.getDrawable(
							itemView.context,
							R.drawable.ic_create_post
						)
					)
				}

				KEY_NOTIFICATION_POST_UPVOTE -> {
					val likedPostTitle = if (notification.objects.isNotEmpty()) {
						notification.objects[0].title?.trim()
							?: notification.objects[0].summary.trim()
					} else {
						root.context.getString(R.string.unknown_article)
					}
					txvNotificationText.text = itemView.context.getString(
						R.string.notification_post_upvote,
						if (notification.from.isNotEmpty()) {
							notification.from[0].profile.name.trim()
						} else {
							root.context.getString(
								R.string.unknown_author)
						},
						likedPostTitle
					)

					imvIcon.setImageDrawable(
						ContextCompat.getDrawable(
							itemView.context,
							R.drawable.ic_favorite
						)
					)
				}

				KEY_NOTIFICATION_REPLY -> {
					txvNotificationText.text = itemView.context.getString(
						R.string.notification_reply,
						if (notification.from.isNotEmpty()) {
							notification.from[0].profile.name.trim()
						} else {
							root.context.getString(
								R.string.unknown_author)
						},
						if (notification.objects[0].onlyParentId()) {
							root.context.getString(R.string.unknown_article)
						} else {
							Gson().fromJson(notification.objects[0].parent,
								Post::class.java).title
						},
						notification.objects[0].summary
					)

					imvIcon.setImageDrawable(
						ContextCompat.getDrawable(
							itemView.context,
							R.drawable.ic_comment
						)
					)
				}

				KEY_NOTIFICATION_FOLLOW_USER -> {
					txvNotificationText.text = itemView.context.getString(
						R.string.notification_follow_user,
						if (notification.from.isNotEmpty()) {
							notification.from[0].profile.name.trim()
						} else {
							root.context.getString(
								R.string.unknown_author)
						}
					)

					imvIcon.setImageDrawable(
						ContextCompat.getDrawable(
							itemView.context,
							R.drawable.ic_follower_black
						)
					)
				}

				else -> {
					txvNotificationText.text = itemView.context.getString(
						R.string.notification_other,
						notification.verb
					)

					imvIcon.setImageDrawable(
						ContextCompat.getDrawable(
							itemView.context,
							R.drawable.ic_view
						)
					)
				}
			}
		}
	}
}