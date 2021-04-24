package khusainov.farrukh.communityapp.data.repository

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.Notification
import khusainov.farrukh.communityapp.utils.Constants.PAGE_STARTING_INDEX

/**
 *Created by FarrukhKhusainov on 4/5/21 5:19 PM
 **/
class NotificationPagingSource(private val communityApiService: CommunityApiService) :
    PagingSource<Int, Notification>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val position = params.key ?: PAGE_STARTING_INDEX

        return try {
            communityApiService.getNotifications(page = position, limit = 50).let {
                LoadResult.Page(
                    data = it,
                    prevKey = if (position == PAGE_STARTING_INDEX) null else position - 1,
                    nextKey = if (it.isEmpty()) null else position + 1
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}