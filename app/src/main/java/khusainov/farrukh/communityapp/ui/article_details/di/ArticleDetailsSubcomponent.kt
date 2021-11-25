package khusainov.farrukh.communityapp.ui.article_details.di

import dagger.BindsInstance
import dagger.Subcomponent
import khusainov.farrukh.communityapp.di.ArticleId
import khusainov.farrukh.communityapp.ui.article_details.view.ArticleFragment
import khusainov.farrukh.communityapp.ui.article_details.view.ReportDialogFragment

/**
 *Created by farrukh_kh on 11/24/21 9:34 PM
 *khusainov.farrukh.communityapp.ui.article_details.di
 **/
@Subcomponent(modules = [ArticleDetailsModule::class])
interface ArticleDetailsSubcomponent {

	@Subcomponent.Factory
	interface Factory {
		fun create(@BindsInstance @ArticleId articleId: String): ArticleDetailsSubcomponent
	}

	fun inject(fragment: ArticleFragment)
	fun inject(fragment: ReportDialogFragment)
}