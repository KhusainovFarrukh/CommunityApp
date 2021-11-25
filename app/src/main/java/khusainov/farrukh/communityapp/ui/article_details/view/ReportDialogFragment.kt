package khusainov.farrukh.communityapp.ui.article_details.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.posts.remote.ReportPostRequest
import khusainov.farrukh.communityapp.databinding.FragmentDialogReportBinding
import khusainov.farrukh.communityapp.getAppComponent
import khusainov.farrukh.communityapp.ui.article_details.viewmodel.ReportViewModel
import javax.inject.Inject

class ReportDialogFragment : DialogFragment() {

	private var _binding: FragmentDialogReportBinding? = null
	private val binding get() = _binding!!

	private val articleId by lazy {
		ReportDialogFragmentArgs.fromBundle(
			requireArguments()).postId
	}

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	private val reportViewModel by viewModels<ReportViewModel> { viewModelFactory }

	override fun onAttach(context: Context) {
		super.onAttach(context)
		getAppComponent().articleDetailsSubcomponent().create(articleId).inject(this)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentDialogReportBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setClickListeners()
		setObservers()
	}

	override fun onCancel(dialog: DialogInterface) {
		super.onCancel(dialog)

		Toast.makeText(
			context,
			getString(R.string.report_window_closed),
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

	private fun setClickListeners() = with(binding) {
		btnReport.setOnClickListener {
			if (etDescription.text.isNotEmpty()) {
				reportViewModel.reportPost(
					articleId,
					ReportPostRequest(
						spTypeOfReport.selectedItem.toString(),
						etDescription.text.toString()
					)
				)
			} else {
				Toast.makeText(
					context,
					getString(R.string.empty_text),
					Toast.LENGTH_LONG
				).show()
			}
		}

		btnCancel.setOnClickListener {
			this@ReportDialogFragment.dismiss()
		}
	}

	private fun setObservers() = with(reportViewModel) {
		//observe report loading state
		isLoading.observe(viewLifecycleOwner) {
			binding.rlLoading.isVisible = it
		}

		//observe whether report is sent or not
		isReported.observe(viewLifecycleOwner) {
			if (it) {
				Toast.makeText(
					requireContext(),
					getString(R.string.done),
					Toast.LENGTH_SHORT
				).show()
				this@ReportDialogFragment.dismiss()
			}
		}

		//observe error while sending report
		reportError.observe(viewLifecycleOwner) { otherError ->
			(Snackbar.make(binding.root, otherError.message, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry)) {
					otherError.retry.invoke()
				}).show()
		}
	}
}