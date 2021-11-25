package khusainov.farrukh.communityapp.ui.topic_details.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.topic_details.viewmodel.TopicViewModel

/**
 *Created by farrukh_kh on 11/24/21 9:43 PM
 *khusainov.farrukh.communityapp.ui.topic_details.di
 **/
@Module
abstract class TopicDetailsModule {

	@Binds
	@IntoMap
	@ViewModelKey(TopicViewModel::class)
	abstract fun bindViewModel(viewModel: TopicViewModel): ViewModel
}