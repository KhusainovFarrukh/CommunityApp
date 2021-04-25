package khusainov.farrukh.communityapp.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import khusainov.farrukh.communityapp.R
import khusainov.farrukh.communityapp.data.models.ReportValue
import khusainov.farrukh.communityapp.databinding.FragmentDialogReportBinding
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.vm.factories.ReportVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ReportViewModel

class ReportDialogFragment : DialogFragment() {

	private var _binding: FragmentDialogReportBinding? = null
	private val binding get() = _binding!!

	private val articleId by lazy {
		arguments?.getString(KEY_ARTICLE_ID)
			?: throw NullPointerException(getString(R.string.no_article_id))
	}

	private val reportViewModel by lazy {
		ViewModelProvider(this, ReportVMFactory(requireContext()))
			.get(ReportViewModel::class.java)
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
					ReportValue(
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