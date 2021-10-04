package khusainov.farrukh.communityapp.data.notifications

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.notifications.remote.Notification
import khusainov.farrukh.communityapp.data.notifications.remote.NotificationsApi
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by farrukh_kh on 10/4/21 10:54 PM
 *khusainov.farrukh.communityapp.data.notifications
 **/
class NotificationPagingSource(private val api: NotificationsApi) :
	PagingSource<Int, Notification>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
		val position = params.key ?: Constants.PAGE_STARTING_INDEX

		return try {
			api.getNotifications(page = position, limit = 50).let {
				LoadResult.Page(
					data = it,
					prevKey = if (position == Constants.PAGE_STARTING_INDEX) null else position - 1,
					nextKey = if (it.isEmpty()) null else position + 1
				)
			}
		} catch (exception: Exception) {
			LoadResult.Error(exception)
		}
	}
}