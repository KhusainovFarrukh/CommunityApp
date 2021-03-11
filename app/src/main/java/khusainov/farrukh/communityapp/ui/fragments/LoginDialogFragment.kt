package khusainov.farrukh.communityapp.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.SignInData
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentDialogLoginBinding
import khusainov.farrukh.communityapp.ui.activities.HomeActivityListener
import khusainov.farrukh.communityapp.vm.factories.LoginVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.LoginViewModel

class LoginDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogLoginBinding? = null
    private val binding get() = _binding!!
    private var activityListener: HomeActivityListener? = null
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginVMFactory(Repository(RetrofitInstance(requireContext()).communityApi))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setClickListeners()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        if (loginViewModel.isLoading.value == true) {
            loginViewModel.cancelJob()
        }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeActivityListener) {
            activityListener = context
        } else {
            throw IllegalArgumentException("$context is not HomeActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
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
                    loginViewModel.signIn(
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
        loginViewModel.responseUser.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {

                activityListener?.saveSignInData(
                    SignInData(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                )
                Toast.makeText(
                    context,
                    "Muvaffaqiyatli kirildi",
                    Toast.LENGTH_SHORT
                ).show()
                this.dismiss()
            } else {
                Toast.makeText(
                    context,
                    "Error code:" + response.code(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.btnLogin.isEnabled = !it
            binding.pbLoading.isVisible = it
        }
    }
}