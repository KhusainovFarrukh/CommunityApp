package khusainov.farrukh.communityapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.databinding.ActivityMainBinding
import khusainov.farrukh.communityapp.model.Article
import khusainov.farrukh.communityapp.viewmodel.ArticleViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        setClickListeners()
        setObservers()

    }

    private fun setObservers() {
        articleViewModel.responseArticle.observe(this, { response ->
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
            binding.btnOk.isEnabled = !it
            binding.pbLoading.isVisible = it
        })
    }

    private fun setClickListeners() {
        binding.btnOk.setOnClickListener {
            if (binding.etLink.text.isNotEmpty()) {
                val link = binding.etLink.text.toString()
                val articleId = link.substring(link.lastIndexOf('-') + 1)

                articleViewModel.getArticle(articleId)
            } else {
                binding.etLink.error = "Maqola linkini yozing"
            }
        }
    }

    private fun setStatsToViews(article: Article) {
        binding.txvTitleValue.text = article.title
        binding.txvLikesValue.text = article.stats.likes.toString()
        binding.txvViewsValue.text = article.stats.views.toString()
        binding.txvFollowersValue.text = article.stats.followers.toString()
        binding.txvCommentsValue.text = article.stats.comments.toString()
    }
}