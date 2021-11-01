package khusainov.farrukh.communityapp.data.notifications

import androidx.paging.*
import khusainov.farrukh.communityapp.data.notifications.remote.NotificationsApi
import javax.inject.Inject

/**
 *Created by farrukh_kh on 10/4/21 10:53 PM
 *khusainov.farrukh.communityapp.data.notifications
 **/
class NotificationsRepository @Inject constructor(private val api: NotificationsApi) {

	//function to get notifications of signed user  (with paging)
	fun getNotifications() = Pager(
		PagingConfig(
			pageSize = 50,
			maxSize = 200,
			enablePlaceholders = false
		),
		pagingSourceFactory = { NotificationPagingSource(api) }
	).liveData
}