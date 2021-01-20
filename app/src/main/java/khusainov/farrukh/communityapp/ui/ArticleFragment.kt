package khusainov.farrukh.communityapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import khusainov.farrukh.communityapp.databinding.FragmentArticleBinding
import org.jsoup.Jsoup

class ArticleFragment : Fragment() {

    private var activityListener: HomeActivityListener? = null
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getString("content", "") ?: ""
        val doc = Jsoup.parse(content)

        doc.select("img").attr("width", "100%")

        binding.wvArticle.loadData(
            doc.html(),
            "text/html",
            "UTF-8"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeActivityListener) {
            activityListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }
}