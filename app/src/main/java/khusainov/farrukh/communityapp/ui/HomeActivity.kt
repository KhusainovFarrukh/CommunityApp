package khusainov.farrukh.communityapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), HomeActivityListener {

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

    override fun goToBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }
}

interface HomeActivityListener {
    fun showLoginDialog()
    fun showMainFragment()
    fun goToBrowser(url: String)
}