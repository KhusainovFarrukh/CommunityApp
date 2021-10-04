package khusainov.farrukh.communityapp.data.notifications.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by farrukh_kh on 10/4/21 10:52 PM
 *khusainov.farrukh.communityapp.data.notifications.remote
 **/
interface NotificationsApi {

	//function to get notifications of signed user  (with paging)
	@GET("api/v1/notifications")
	suspend fun getNotifications(
		@Query("page") page: Int = 1,
		@Query("limit") limit: Int = 50,
	): List<Notification>
}