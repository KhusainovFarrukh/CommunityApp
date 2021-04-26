package khusainov.farrukh.communityapp.utils

object Constants {
	//api base url
	const val BASE_URL = "https://community.uzbekcoders.uz"

	//cookies keys for sending and getting
	const val KEY_COOKIES_RESPONSE = "Set-Cookie"
	const val KEY_COOKIES_REQUEST = "Cookie"

	//cookies
	const val KEY_CSRF_TOKEN = "CSRF-Token"
	const val KEY_REMEMBER_ME = "remember_me"
	const val KEY_SESSION_ID = "sessionId"
	const val KEY_CSRF = "_csrf"

	const val DELIMITER_COOKIES = ";"
	const val DELIMITER_CSRF = "="

	//index for Paging3
	const val PAGE_STARTING_INDEX = 1

	//keys for SharedPreferences and Bundle
	const val KEY_ARTICLE_ID = "articleId"
	const val KEY_USER_ID = "userId"
	const val KEY_TOPIC_ID = "topicId"
	const val KEY_SIGN_IN_DATA = "sign_in_data"

	//keys for notification types
	const val KEY_NOTIFICATION_POST = "post"
	const val KEY_NOTIFICATION_POST_UPVOTE = "post_upvote"
	const val KEY_NOTIFICATION_REPLY = "reply"
	const val KEY_NOTIFICATION_FOLLOW_USER = "follow_user"

	//sortBy values
	const val SORT_BY_TIME_DESC = "createdAt.desc"
	const val SORT_BY_TIME_ASC = "createdAt.asc"
	const val SORT_BY_UPVOTES = "upvotes"

	const val KEY_TYPE = "type"
	const val KEY_SORT_BY = "sortBy"
	const val KEY_ARTICLE = "article"
	const val KEY_RESPONSE = "response"

	const val TITLE_ARTICLES = "Articles"
	const val TITLE_COMMENTS = "Comments"

	const val TYPE_INTENT_TEXT = "text/plain"
	const val VALUE_DEFAULT = ""
}