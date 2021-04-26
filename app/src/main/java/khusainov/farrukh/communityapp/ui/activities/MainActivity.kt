package khusainov.farrukh.communityapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.Post
import khusainov.farrukh.communityapp.data.models.SignInData
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding
import khusainov.farrukh.communityapp.ui.fragments.*
import khusainov.farrukh.communityapp.utils.Constants.BASE_URL
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.utils.Constants.KEY_SIGN_IN_DATA
import khusainov.farrukh.communityapp.utils.Constants.KEY_TOPIC_ID
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID
import khusainov.farrukh.communityapp.utils.Constants.TYPE_INTENT_TEXT
import khusainov.farrukh.communityapp.utils.Constants.VALUE_DEFAULT
import khusainov.farrukh.communityapp.utils.listeners.HomeActivityListener

@SuppressLint("CommitPrefEdits")
class HomeActivity : AppCompatActivity(), HomeActivityListener {

    //sharedPrefs for saving some data (user email and password, api headers adn etc.)
    private val sharedPreferences by lazy { getPreferences(MODE_PRIVATE) }
    private val editor by lazy { sharedPreferences.edit() }
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //if activity is not re-created, show MainFragment
        if (savedInstanceState == null) {
            showMainFragment()
        }
    }

    override fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, MainFragment())
            .commit()
    }

    override fun showLoginDialog() {
        LoginDialogFragment().show(supportFragmentManager, null)
    }

    override fun showNotificationsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, NotificationsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun showArticleDetailsFragment(articleId: String) {
        val fragment = ArticleFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARTICLE_ID, articleId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun saveSignInData(value: SignInData) {
        try {
            editor.putString(KEY_SIGN_IN_DATA, Gson().toJson(value)).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getSignInData() = try {
        Gson().fromJson(
            sharedPreferences.getString(KEY_SIGN_IN_DATA, null),
            SignInData::class.java
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override fun saveUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId).commit()
    }

    override fun getUserId() =
        sharedPreferences.getString(KEY_USER_ID, VALUE_DEFAULT) ?: VALUE_DEFAULT

    override fun showUserFragment(userId: String) {
        val fragment = UserFragment()
        val bundle = Bundle()
        bundle.putString(KEY_USER_ID, userId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun shareIntent(article: Post) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = TYPE_INTENT_TEXT
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_article,
                article.user?.profile?.name ?: getString(R.string.unknown_author),
                article.title ?: getString(R.string.comment),
                BASE_URL + article.url)
        )
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_title)))
    }

    override fun showTopicFragment(topicId: String) {
        val fragment = TopicFragment()
        val bundle = Bundle()
        bundle.putString(KEY_TOPIC_ID, topicId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun showReportDialog(articleId: String) {
        val fragment = ReportDialogFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ARTICLE_ID, articleId)
        fragment.arguments = bundle

        fragment.show(supportFragmentManager, null)
    }
}