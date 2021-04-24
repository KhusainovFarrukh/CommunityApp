package khusainov.farrukh.communityapp.vm.viewmodels

import androidx.lifecycle.ViewModel
import khusainov.farrukh.communityapp.data.repository.Repository

/**
 *Created by FarrukhKhusainov on 3/18/21 9:55 PM
 **/
class PostsOfUserViewModel(
    userId: String,
    type: String,
    sortBy: String,
    repository: Repository,
) : ViewModel() {

    val usersPosts = repository.getPostsOfUser(userId, type, sortBy)
}