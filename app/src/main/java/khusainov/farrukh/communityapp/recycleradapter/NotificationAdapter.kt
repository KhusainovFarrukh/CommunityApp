package khusainov.farrukh.communityapp.recycleradapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.callback.NotificationCallback
import khusainov.farrukh.communityapp.clicklistener.NotificationClickListener
import khusainov.farrukh.communityapp.model.Notification

class NotificationAdapter(private val notificationClickListener: NotificationClickListener) :
    ListAdapter<Notification, NotificationViewHolder>(NotificationCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            notificationClickListener.onNotificationClick(getItem(position))
        }
        holder.onBindArticle(getItem(position))
    }
}

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val txvText = itemView.findViewById<TextView>(R.id.txv_notification_text)
    private val imvIcon = itemView.findViewById<ImageView>(R.id.imv_icon)

    fun onBindArticle(notification: Notification) {
        if (!notification.isRead) {
            txvText.typeface = Typeface.DEFAULT_BOLD
        }

        when (notification.verb) {
            "post" -> {
                var tempString: String? = ""
                if (notification.objects.isNotEmpty()) {
                    tempString = notification.objects[0].title
                }
                txvText.text = itemView.context.getString(
                    R.string.verb_post,
                    notification.from[0].profile.name,
                    tempString
                )
                imvIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_create_post
                    )
                )
            }
            "post_upvote" -> {
                val tempString = notification.objects[0].title ?: notification.objects[0].summary
                txvText.text = itemView.context.getString(
                    R.string.verb_post_upvote,
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
            "reply" -> {
                txvText.text = itemView.context.getString(
                    R.string.verb_reply,
                    notification.from[0].profile.name,
                    notification.objects[0].parent.title,
                    notification.objects[0].summary
                )
                imvIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_comment
                    )
                )
            }
            "follow_user" -> {
                txvText.text = itemView.context.getString(
                    R.string.verb_follow_user,
                    notification.from[0].profile.name
                )
                imvIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_follower
                    )
                )
            }
            else -> {
                txvText.text = itemView.context.getString(
                    R.string.verb_else,
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