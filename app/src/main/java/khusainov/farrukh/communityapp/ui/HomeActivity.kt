package khusainov.farrukh.communityapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.databinding.ActivityHomeBinding
import khusainov.farrukh.communityapp.model.SignInData

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
            .replace(R.id.view_container, NotificationsFragment())
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

    override fun saveCookies(value: Map<String, String>) {
        editor.putString("cookies", Gson().toJson(value)).apply()
    }

    override fun getCookies(): Map<String, String>? {
        return Gson().fromJson(
            sharedPreferences.getString("cookies", null),
            object : TypeToken<Map<String, String>>() {}.type
        )
    }
}

interface HomeActivityListener {
    fun showLoginDialog()
    fun showMainFragment()
    fun goToBrowser(url: String)
    fun showNotificationsFragment()
    fun saveSignInData(value: SignInData)
    fun getSignInData(): SignInData?
    fun saveCookies(value: Map<String, String>)
    fun getCookies(): Map<String, String>?
}