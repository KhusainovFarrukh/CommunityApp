package khusainov.farrukh.communityapp.vm.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.*

/**
 *Created by FarrukhKhusainov on 3/4/21 11:02 PM
 **/
open class BaseViewModelFactory(
    private val viewModelType: String,
    private val id: String = "",
    context: Context,
) : ViewModelProvider.Factory {

    private val repository = Repository(RetrofitInstance(context).communityApiService)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (viewModelType) {
            "user" -> UserViewModel(id, repository) as T
            "article" -> ArticleViewModel(id, repository) as T
            "topic" -> TopicViewModel(id, repository) as T
            "main" -> MainViewModel(repository) as T
            "login" -> LoginViewModel(repository) as T
            "notification" -> NotificationsViewModel(repository) as T
            "report" -> ReportViewModel(repository) as T
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