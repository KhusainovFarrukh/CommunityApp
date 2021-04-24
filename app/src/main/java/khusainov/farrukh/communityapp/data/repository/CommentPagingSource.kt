package khusainov.farrukh.communityapp.data.repository

import androidx.paging.PagingSource
import khusainov.farrukh.communityapp.data.api.CommunityApiService
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.utils.Constants

/**
 *Created by FarrukhKhusainov on 4/5/21 5:52 PM
 **/
class CommentPagingSource(
    private val articleId: String,
    private val communityApiService: CommunityApiService,
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val position = params.key ?: Constants.PAGE_STARTING_INDEX

        return try {
            communityApiService.getCommentsOfArticle(
                page = position,
                limit = 25,
                articleId = articleId,
            ).let {
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