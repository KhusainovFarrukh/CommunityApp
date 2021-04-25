package khusainov.farrukh.communityapp.ui.adapters.recycler

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
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
import khusainov.farrukh.communityapp.utils.clicklisteners.ItemClickListener

class NotificationAdapter(private val notificationClickListener: ItemClickListener) :
	PagingDataAdapter<Notification, NotificationAdapter.NotificationViewHolder>(object :
        DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem == newItem
    }) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
		return NotificationViewHolder(
            ViewholderNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
	}

	override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
		holder.itemView.setOnClickListener {
			notificationClickListener.onNotificationClick(getItem(position)!!)
		}
		holder.onBindArticle(getItem(position)!!)
	}

	inner class NotificationViewHolder(private val binding: ViewholderNotificationBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBindArticle(notification: Notification) {
			binding.apply {
				if (notification.isRead) {
					txvNotificationText.typeface = Typeface.DEFAULT
				} else {
					txvNotificationText.typeface = Typeface.DEFAULT_BOLD
				}

				when (notification.verb) {
                    KEY_NOTIFICATION_POST -> {
                        val tempString = if (notification.objects.isNotEmpty()) {
                            notification.objects[0].title
                        } else {
                            "Noma`lum maqolani"
                        }
                        txvNotificationText.text = itemView.context.getString(
                            R.string.notification_post,
                            if (notification.from.isNotEmpty()) notification.from[0].profile.name else "Unknown",
                            tempString
                        )
                        imvIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.ic_create_post
                            )
                        )
                    }
                    KEY_NOTIFICATION_POST_UPVOTE -> {
                        val tempString = if (notification.objects.isNotEmpty()) {
                            notification.objects[0].title ?: notification.objects[0].summary
                        } else {
                            "Noma`lum maqolaga"
                        }
                        txvNotificationText.text = itemView.context.getString(
                            R.string.notification_post_upvote,
                            notification.from[0].profile.name,
                            tempString
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
                            notification.from[0].profile.name,
                            if (notification.objects[0].onlyParentId()) {
                                "Maqola"
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
                            notification.from[0].profile.name
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
}