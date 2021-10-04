package khusainov.farrukh.communityapp.utils.vm_factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.ui.article_details.viewmodel.ArticleViewModel
import khusainov.farrukh.communityapp.ui.article_details.viewmodel.ReportViewModel
import khusainov.farrukh.communityapp.ui.auth.viewmodel.LoginViewModel
import khusainov.farrukh.communityapp.ui.home.viewmodel.HomeViewModel
import khusainov.farrukh.communityapp.ui.notifications.viewmodel.NotificationsViewModel
import khusainov.farrukh.communityapp.ui.topic_details.viewmodel.TopicViewModel
import khusainov.farrukh.communityapp.ui.user.viewmodel.UserViewModel

/**
 *Created by FarrukhKhusainov on 3/4/21 11:02 PM
 **/
//base factory for other ViewModelFactories (but not for PostsOfUserVMFactory)
open class BaseViewModelFactory(
    private val viewModelType: String,
    private val id: String = "",
    private val context: Context,
) : ViewModelProvider.Factory {

	private val repository = Repository(RetrofitInstance(context).communityApiService)

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return when (viewModelType) {
			//return ViewModel depending on type (excluding postsOfUser type)
            context.resources.getStringArray(R.array.vm_factory_types)[0] -> UserViewModel(id,
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[1] -> ArticleViewModel(id,
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[2] -> TopicViewModel(id,
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[3] -> HomeViewModel(
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[4] -> LoginViewModel(
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[5] -> NotificationsViewModel(
                repository) as T
            context.resources.getStringArray(R.array.vm_factory_types)[6] -> ReportViewModel(
                repository) as T
			else -> throw IllegalArgumentException("$viewModelType is not supported")
		}
	}
}

class ArticleVMFactory(articleId: String, context: Context) :
	BaseViewModelFactory("article", articleId, context)

class UserVMFactory(userId: String, context: Context) :
	BaseViewModelFactory("user", userId, context)

class TopicVMFactory(topicId: String, context: Context) :
	BaseViewModelFactory("topic", topicId, context)

class MainVMFactory(context: Context) :
	BaseViewModelFactory("main", context = context)

class LoginVMFactory(context: Context) :
	BaseViewModelFactory("login", context = context)

class NotificationsVMFactory(context: Context) :
	BaseViewModelFactory("notification", context = context)

class ReportVMFactory(context: Context) :
	BaseViewModelFactory("report", context = context)