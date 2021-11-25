package khusainov.farrukh.communityapp.ui.article_details.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import khusainov.farrukh.communityapp.di.ViewModelKey
import khusainov.farrukh.communityapp.ui.article_details.viewmodel.ArticleViewModel
import khusainov.farrukh.communityapp.ui.article_details.viewmodel.ReportViewModel

/**
 *Created by farrukh_kh on 11/24/21 9:35 PM
 *khusainov.farrukh.communityapp.ui.article_details.di
 **/
@Module
abstract class ArticleDetailsModule {

	@Binds
	@IntoMap
	@ViewModelKey(ArticleViewModel::class)
	abstract fun bindArticleViewModel(viewModel: ArticleViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(ReportViewModel::class)
	abstract fun bindReportViewModel(viewModel: ReportViewModel): ViewModel
}