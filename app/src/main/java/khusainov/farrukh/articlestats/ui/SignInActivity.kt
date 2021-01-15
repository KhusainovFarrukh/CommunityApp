package khusainov.farrukh.articlestats.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.articlestats.R
import khusainov.farrukh.articlestats.databinding.ActivitySignInBinding
import khusainov.farrukh.articlestats.model.Notif
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import khusainov.farrukh.articlestats.viewmodel.ArticleViewModel
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.URL

class SignInActivity : AppCompatActivity() {

    private lateinit var cookieRememberMe: Cookie
    private lateinit var cookieSessionId: Cookie

    private lateinit var binding: ActivitySignInBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        setClickListeners()
        setObservers()

    }

    private fun setObservers() {
        articleViewModel.responseUser.observe(this, { response ->
            if (response.isSuccessful) {

                if (response.raw().headers("set-cookie").isNotEmpty()) {
                    cookieRememberMe = Cookie.Companion.parse(
                        response.raw().request.url,
                        response.raw().headers("set-cookie")[0]
                    )!!

                    cookieSessionId = Cookie.Companion.parse(
                        response.raw().request.url,
                        response.raw().headers("set-cookie")[1]
                    )!!
                } else {
                    Log.wtf("Cookie", "There is no cookies")
                }

                setStatsToViews(response.body()!!)

            } else {
                Toast.makeText(
                    this,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        articleViewModel.isLoading.observe(this, {
            binding.btnSignIn.isEnabled = !it
            binding.pbLoading.isVisible = it
        })

        articleViewModel.responseNotif.observe(this, { response ->
            if (response.isSuccessful) {
                Toast.makeText(
                    this,
                    response.body()?.get(0)?.verb ?: "Something is null",
                    Toast.LENGTH_SHORT
                ).show()

                setNotifToViews(response.body()!!)

            } else {
                Toast.makeText(
                    this,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setClickListeners() {
        binding.btnSignIn.setOnClickListener {
            when {
                binding.etEmail.text.isEmpty() && binding.etPassword.text.isEmpty() -> {
                    binding.etEmail.error = "Email manzilingizni yozing"
                    binding.etPassword.error = "Parolingizni kiriting"
                }
                binding.etEmail.text.isEmpty() -> binding.etEmail.error =
                    "Email manzilingizni yozing"
                binding.etPassword.text.isEmpty() -> binding.etPassword.error =
                    "Parolingizni kiriting"
                else -> {
                    articleViewModel.signIn(
                        SignInData(
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString()
                        )
                    )
                }
            }
        }

        binding.btnGetNotif.setOnClickListener {
            articleViewModel.getNotifications(
                cookieRememberMe.name + "=" + cookieRememberMe.value,
                cookieSessionId.name + "=" + cookieSessionId.value
            )
        }
    }

    private fun setStatsToViews(user: User) {
        binding.txvName.text = user.profile.name
        binding.txvEmail.text = user.email
        binding.txvScoreValue.text = user.profile.score.toString()
        binding.txvArticlesValue.text = user.profile.userStats.articles.toString()
        binding.txvLikesValue.text = user.profile.userStats.likes.toString()
        binding.txvFollowersValue.text = user.profile.userStats.followers.toString()

        binding.imvProfilePhoto.load(user.profile.picture) {
            crossfade(true)
            placeholder(R.drawable.ic_account_circle)
            transformations(CircleCropTransformation())
        }
    }

    private fun setNotifToViews(notifList: List<Notif>) {
        notifList.forEach {
            if (!it.read) {
                binding.txvNotif.append("${it.verb} turidagi xabar\n")
            }
        }
    }
}