package khusainov.farrukh.communityapp.ui.auth.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.auth.AuthRepository
import khusainov.farrukh.communityapp.data.auth.remote.SignInRequest
import khusainov.farrukh.communityapp.databinding.FragmentDialogLoginBinding
import khusainov.farrukh.communityapp.getAppComponent
import khusainov.farrukh.communityapp.ui.auth.viewmodel.LoginViewModel
import khusainov.farrukh.communityapp.ui.auth.viewmodel.LoginViewModelFactory
import khusainov.farrukh.communityapp.utils.listeners.HomeActivityListener
import javax.inject.Inject

class LoginDialogFragment : DialogFragment() {

	private var _binding: FragmentDialogLoginBinding? = null
	private val binding get() = _binding!!
	private var activityListener: HomeActivityListener? = null

	@Inject
	lateinit var authRepository: AuthRepository
	private val loginViewModel: LoginViewModel by activityViewModels {
		LoginViewModelFactory(authRepository)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentDialogLoginBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		getAppComponent().inject(this)
		setObservers()
		setClickListeners()
	}

	override fun onCancel(dialog: DialogInterface) {
		super.onCancel(dialog)

		Toast.makeText(
			context,
			getString(R.string.login_window_closed),
			Toast.LENGTH_SHORT
		).show()
	}

	override fun onStart() {
		super.onStart()
		val width = (resources.displayMetrics.widthPixels * 1)
		dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
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
			throw IllegalArgumentException(getString(R.string.context_is_not_listener,
				context.toString()))
		}
	}

	override fun onDetach() {
		super.onDetach()
		activityListener = null
	}

	private fun setClickListeners() = with(binding) {
		btnLogin.setOnClickListener {
			when {
				etEmail.text.isEmpty() && etPassword.text.isEmpty() -> {
					etEmail.error = getString(R.string.error_et_email)
					etPassword.error = getString(R.string.error_et_password)
				}

				etEmail.text.isEmpty() -> etEmail.error =
					getString(R.string.error_et_email)

				etPassword.text.isEmpty() -> etPassword.error =
					getString(R.string.error_et_password)

				else -> {
					loginViewModel.signInWithEmail(
						SignInRequest(
							etEmail.text.toString(),
							etPassword.text.toString()
						)
					)
				}
			}
		}
	}

	private fun setObservers() = with(loginViewModel) {
		//observe signing in state
		isLoading.observe(viewLifecycleOwner) {
			binding.btnLogin.isEnabled = !it
			binding.pbLoading.isVisible = it
		}

		//observe signed user
		userLiveData.observe(viewLifecycleOwner) {
			activityListener?.saveSignInData(
				SignInRequest(
					binding.etEmail.text.toString(),
					binding.etPassword.text.toString()
				)
			)
			Toast.makeText(
				context,
				getString(R.string.login_successful),
				Toast.LENGTH_SHORT
			).show()
			this@LoginDialogFragment.dismiss()
		}

		//observe error while signing in
		signInError.observe(viewLifecycleOwner) { otherError ->
			(Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry)) {
					otherError.retry.invoke()
				}).show()
		}
	}
}