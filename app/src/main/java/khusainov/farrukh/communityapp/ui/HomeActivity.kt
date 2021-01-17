package khusainov.farrukh.communityapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding
import khusainov.farrukh.communityapp.model.Notif
import khusainov.farrukh.communityapp.model.SignInData
import khusainov.farrukh.communityapp.model.User
import khusainov.farrukh.communityapp.utils.Constants.Companion.COOKIES_KEY
import khusainov.farrukh.communityapp.utils.Constants.Companion.REMEMBER_ME_KEY
import khusainov.farrukh.communityapp.utils.Constants.Companion.SESSION_ID_KEY
import khusainov.farrukh.communityapp.viewmodel.ArticleViewModel
import okhttp3.Cookie

class HomeActivity : AppCompatActivity(), HomeActivityListener {

    val cookies = HashMap<String, String>()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            showMainFragment()
        }
    }

    override fun showLoginDialog() {
        LoginDialogFragment().show(supportFragmentManager, null)
    }

    override fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_container, MainFragment())
            .commit()
    }
}

interface HomeActivityListener {
    fun showLoginDialog()
    fun showMainFragment()

}