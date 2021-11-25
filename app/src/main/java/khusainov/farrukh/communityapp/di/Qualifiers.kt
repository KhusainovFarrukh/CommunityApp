package khusainov.farrukh.communityapp.di

import javax.inject.Qualifier

/**
 *Created by farrukh_kh on 11/24/21 10:19 PM
 *khusainov.farrukh.communityapp.di
 **/

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ArticleId(
	val value: String = "article_id",
)

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class TopicId(
	val value: String = "topic_id",
)

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class UserId(
	val value: String = "user_id",
)

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Type(
	val value: String = "type",
)

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SortBy(
	val value: String = "sort_by",
)