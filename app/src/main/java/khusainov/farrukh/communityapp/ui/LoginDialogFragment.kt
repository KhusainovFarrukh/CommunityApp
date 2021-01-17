package khusainov.farrukh.communityapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import khusainov.farrukh.communityapp.databinding.FragmentDialogLoginBinding
import khusainov.farrukh.communityapp.model.SignInData
import khusainov.farrukh.communityapp.utils.Constants
import khusainov.farrukh.communityapp.viewmodel.ArticleViewModel
import okhttp3.Cookie

class LoginDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogLoginBinding.inflate(inflater)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setClickListeners()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        Toast.makeText(
            context,
            "Kirish oynasi yopildi",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 1)
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
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

    private fun setObservers() {
        articleViewModel.responseUser.observe(this, { response ->
            if (response.isSuccessful) {

                if (response.raw().headers(Constants.COOKIES_KEY).isNotEmpty()) {

                    response.raw().headers(Constants.COOKIES_KEY).forEach {
                        val cookie = Cookie.Companion.parse(
                            response.raw().request.url,
                            it
                        )!!
                        (activity as HomeActivity).cookies[cookie.name] = cookie.value
                    }

                } else {
                    Toast.makeText(
                        context,
                        "There is no cookies",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                (activity as HomeActivity).setStatsToViews(response.body()!!)
                this.dismiss()

            } else {
                Toast.makeText(
                    context,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        articleViewModel.isLoading.observe(this, {
            binding.btnLogin.isEnabled = !it
            binding.pbLoading.isVisible = it
        })
    }
}