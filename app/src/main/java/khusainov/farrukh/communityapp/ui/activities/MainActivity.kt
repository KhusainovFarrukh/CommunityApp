package khusainov.farrukh.communityapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.ArticleDetails
import khusainov.farrukh.communityapp.data.models.SignInData
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding
import khusainov.farrukh.communityapp.ui.fragments.*
import khusainov.farrukh.communityapp.utils.Constants.BASE_URL
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.utils.Constants.KEY_USER_ID

@SuppressLint("CommitPrefEdits")
class HomeActivity : AppCompatActivity(), HomeActivityListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (savedInstanceState == null) {
            showArticlesListFragment()
        }
    }

    override fun showLoginDialog() {
        LoginDialogFragment().show(supportFragmentManager, null)
    }

    override fun showArticlesListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, MainFragment())
            .commit()
    }

    override fun goToBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }

    override fun showNotificationsFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.view_container, NotificationsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun showArticleDetailsFragment(articleId: String) {
        val fragment = ArticleFragment()
        val bundle = Bundle()
        bundle.putString("articleId", articleId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun saveSignInData(value: SignInData) {
        editor.putString("sign_in_data", Gson().toJson(value)).apply()
    }

    override fun getSignInData(): SignInData? {
        return Gson().fromJson(
            sharedPreferences.getString("sign_in_data", null),
            SignInData::class.java
        )
    }

    override fun saveUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId).commit()
    }

    override fun getUserId() = sharedPreferences.getString(KEY_USER_ID, "") ?: ""

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

    override fun shareIntent(article: ArticleDetails) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${article.user?.profile?.name ?: "Noma`lum muallif"} yozgan \"${
                article.title
            }\" nomli maqolani o`qib ko`ring: \n${
                BASE_URL + article.url
            }"
        )
        startActivity(Intent.createChooser(shareIntent, "Share this article with..."))
    }

    override fun showTopicFragment(topicId: String) {
        val fragment = TopicFragment()
        val bundle = Bundle()
        bundle.putString("topicId", topicId)
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

interface HomeActivityListener {
    fun showLoginDialog()
    fun showReportDialog(articleId: String)
    fun showArticlesListFragment()
    fun goToBrowser(url: String)
    fun showNotificationsFragment()
    fun showArticleDetailsFragment(articleId: String)
    fun saveSignInData(value: SignInData)
    fun getSignInData(): SignInData?
    fun saveUserId(userId: String)
    fun getUserId(): String
    fun showUserFragment(userId: String)
    fun shareIntent(article: ArticleDetails)
    fun showTopicFragment(topicId: String)
}