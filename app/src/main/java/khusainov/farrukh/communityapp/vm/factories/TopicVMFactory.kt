package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.TopicViewModel

/**
 *Created by FarrukhKhusainov on 3/23/21 12:04 AM
 **/
class TopicVMFactory(private val topicId: String, private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopicViewModel(repository, topicId) as T
    }
}