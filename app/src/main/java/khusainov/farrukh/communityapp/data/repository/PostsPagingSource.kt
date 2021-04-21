package khusainov.farrukh.communityapp.data.repository

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.utils.Constants.PAGE_STARTING_INDEX

/**
 *Created by FarrukhKhusainov on 3/25/21 1:32 AM
 **/
class PostsPagingSource(private val communityApiService: CommunityApiService) :
    PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val position = params.key ?: PAGE_STARTING_INDEX

        return try {
            communityApiService.getArticlesList(page = position).let {
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