package khusainov.farrukh.communityapp.ui.topic_details.di

import dagger.BindsInstance
import dagger.Subcomponent
import khusainov.farrukh.communityapp.di.TopicId
import khusainov.farrukh.communityapp.ui.topic_details.view.TopicFragment

/**
 *Created by farrukh_kh on 11/24/21 9:43 PM
 *khusainov.farrukh.communityapp.ui.topic_details.di
 **/
@Subcomponent(modules = [TopicDetailsModule::class])
interface TopicDetailsSubcomponent {

	@Subcomponent.Factory
	interface Factory {
		fun create(@BindsInstance @TopicId topicId: String): TopicDetailsSubcomponent
	}

	fun inject(fragment: TopicFragment)
}