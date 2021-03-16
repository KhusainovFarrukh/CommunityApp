package khusainov.farrukh.communityapp.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.vm.viewmodels.ReportViewModel

/**
 *Created by FarrukhKhusainov on 3/16/21 4:24 PM
 **/
class ReportVMFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReportViewModel(repository) as T
    }
}