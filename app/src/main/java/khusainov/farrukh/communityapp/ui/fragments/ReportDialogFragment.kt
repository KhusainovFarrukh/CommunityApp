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
import khusainov.farrukh.communityapp.data.api.RetrofitInstance
import khusainov.farrukh.communityapp.data.model.ReportValue
import khusainov.farrukh.communityapp.data.repository.Repository
import khusainov.farrukh.communityapp.databinding.FragmentDialogReportBinding
import khusainov.farrukh.communityapp.utils.Constants.KEY_ARTICLE_ID
import khusainov.farrukh.communityapp.vm.factories.ReportVMFactory
import khusainov.farrukh.communityapp.vm.viewmodels.ReportViewModel

class ReportDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleId: String
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogReportBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleId = arguments?.getString(KEY_ARTICLE_ID)
            ?: throw NullPointerException("There is no article ID")

        reportViewModel =
            ViewModelProvider(
                this,
                ReportVMFactory(Repository(RetrofitInstance(requireContext()).communityApi))
            ).get(
                ReportViewModel::class.java
            )

        setClickListeners()
        setObservers()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        Toast.makeText(
            context,
            "Report oynasi yopildi",
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

    private fun setClickListeners() {
        binding.apply {
            btnReport.setOnClickListener {
                if (etDescription.text.isNotEmpty()) {
                    reportViewModel.reportArticle(
                        articleId,
                        ReportValue(
                            spTypeOfReport.selectedItem.toString(),
                            etDescription.text.toString()
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Description is EMPTY",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            btnCancel.setOnClickListener {
                this@ReportDialogFragment.dismiss()
            }
        }
    }

    private fun setObservers() {
        reportViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.rlLoading.isVisible = it
            if (!it) {
                Toast.makeText(
                    requireContext(),
                    "Done",
                    Toast.LENGTH_SHORT
                ).show()
                this@ReportDialogFragment.dismiss()
            }
        }
    }
}