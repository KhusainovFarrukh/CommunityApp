package khusainov.farrukh.communityapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.databinding.ActivityMainBinding
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.responseArticle.observe(this, { response ->
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

        mainViewModel.isLoadingLogin.observe(this, {
            binding.btnOk.isEnabled = !it
            binding.pbLoading.isVisible = it
        })
    }

    private fun setClickListeners() {
        binding.btnOk.setOnClickListener {
            if (binding.etLink.text.isNotEmpty()) {
                val link = binding.etLink.text.toString()
                val articleId = link.substring(link.lastIndexOf('-') + 1)

                mainViewModel.getArticle(articleId)
            } else {
                binding.etLink.error = "Maqola linkini yozing"
            }
        }
    }

    private fun setStatsToViews(article: Article) {
        binding.txvTitleValue.text = article.title
        binding.txvLikesValue.text = article.stats.likesCount.toString()
        binding.txvViewsValue.text = article.stats.viewsCount.toString()
        binding.txvFollowersValue.text = article.stats.followersCount.toString()
        binding.txvCommentsValue.text = article.stats.commentsCount.toString()
    }
}