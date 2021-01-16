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

class HomeActivity : AppCompatActivity() {

    private val cookies = HashMap<String, String>()

    private lateinit var binding: ActivityHomeBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        setClickListeners()
        setObservers()

    }

    private fun setObservers() {
        articleViewModel.responseUser.observe(this, { response ->
            if (response.isSuccessful) {

                if (response.raw().headers(COOKIES_KEY).isNotEmpty()) {

                    response.raw().headers(COOKIES_KEY).forEach {
                        val cookie = Cookie.Companion.parse(
                            response.raw().request.url,
                            it
                        )!!
                        cookies[cookie.name] = cookie.value
                    }

                } else {
                    Toast.makeText(
                        this,
                        "There is no cookies",
                        Toast.LENGTH_SHORT
                    ).show()
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
            binding.btnLogin.isEnabled = !it
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
        binding.btnLogin.setOnClickListener {
            articleViewModel.signIn(
                        SignInData(
                            "temp_login",
                            "temp_password"
                        )
                    )
//            when {
//                binding.etEmail.text.isEmpty() && binding.etPassword.text.isEmpty() -> {
//                    binding.etEmail.error = "Email manzilingizni yozing"
//                    binding.etPassword.error = "Parolingizni kiriting"
//                }
//                binding.etEmail.text.isEmpty() -> binding.etEmail.error =
//                    "Email manzilingizni yozing"
//                binding.etPassword.text.isEmpty() -> binding.etPassword.error =
//                    "Parolingizni kiriting"
//                else -> {
//                    articleViewModel.signIn(
//                        SignInData(
//                            binding.etEmail.text.toString(),
//                            binding.etPassword.text.toString()
//                        )
//                    )
//                }
//            }
        }

        binding.imvNotif.setOnClickListener {
            articleViewModel.getNotifications(
                REMEMBER_ME_KEY + "=" + cookies[REMEMBER_ME_KEY],
                SESSION_ID_KEY + "=" + cookies[SESSION_ID_KEY]
            )
        }
    }

    private fun setStatsToViews(user: User) {
//        binding.txvName.text = user.profile.name
//        binding.txvEmail.text = user.email
//        binding.txvScoreValue.text = user.profile.score.toString()
//        binding.txvArticlesValue.text = user.profile.userStats.articles.toString()
//        binding.txvLikesValue.text = user.profile.userStats.likes.toString()
//        binding.txvFollowersValue.text = user.profile.userStats.followers.toString()
        Log.wtf("Name", user.profile.name)
        Log.wtf("Email", user.email)
        Log.wtf("Score", user.profile.score.toString())
        Log.wtf("Posts", user.profile.userStats.posts.toString())
        Log.wtf("Likes", user.profile.userStats.likes.toString())
        Log.wtf("Followers", user.profile.userStats.followers.toString())

        binding.btnLogin.visibility = Button.INVISIBLE
        binding.imvProfile.visibility = ImageView.VISIBLE
        binding.imvCreatePost.visibility = ImageView.VISIBLE

        binding.imvProfile.load(user.profile.picture) {
            crossfade(true)
            placeholder(R.drawable.ic_account_circle)
            transformations(CircleCropTransformation())
        }
    }

    private fun setNotifToViews(notifList: List<Notif>) {
        notifList.forEach {
            if (!it.read) {
//                binding.txvNotif.append("${it.verb} turidagi xabar\n")
                Log.wtf("Notif", "${it.verb} turidagi xabar")
            }
        }
    }
}