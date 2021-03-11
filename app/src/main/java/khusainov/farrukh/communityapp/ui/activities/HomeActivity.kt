package khusainov.farrukh.communityapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.model.SignInData
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding
import khusainov.farrukh.communityapp.ui.fragments.*

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
            showMainFragment()
        }
    }

    override fun showLoginDialog() {
        LoginDialogFragment().show(supportFragmentManager, null)
    }

    override fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, ArticlesListFragment())
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

    override fun showArticleFragment(articleId: String) {
        val fragment = ArticleDetailsFragment()
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

    override fun showUserFragment(userId: String) {
        val fragment = UserFragment()
        val bundle = Bundle()
        bundle.putString("userId", userId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

interface HomeActivityListener {
    fun showLoginDialog()
    fun showMainFragment()
    fun goToBrowser(url: String)
    fun showNotificationsFragment()
    fun showArticleFragment(articleId: String)
    fun saveSignInData(value: SignInData)
    fun getSignInData(): SignInData?
    fun showUserFragment(userId: String)
}