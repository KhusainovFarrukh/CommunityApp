package khusainov.farrukh.articlestats.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import khusainov.farrukh.articlestats.R
import khusainov.farrukh.articlestats.databinding.ActivityMainBinding
import khusainov.farrukh.articlestats.databinding.ActivitySignInBinding
import khusainov.farrukh.articlestats.model.Article
import khusainov.farrukh.articlestats.model.SignInData
import khusainov.farrukh.articlestats.model.User
import khusainov.farrukh.articlestats.viewmodel.ArticleViewModel

class SignInActivity : AppCompatActivity() {

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
}