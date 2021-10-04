package khusainov.farrukh.communityapp.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import khusainov.farrukh.communityapp.databinding.ViewholderLoadStateBinding

/**
 *Created by FarrukhKhusainov on 3/25/21 2:09 AM
 **/
class ListLoadStateAdapter(private val retry: () -> Unit) :
	LoadStateAdapter<ListLoadStateAdapter.ListLoadStateViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
		ListLoadStateViewHolder(
			ViewholderLoadStateBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)

	override fun onBindViewHolder(holder: ListLoadStateViewHolder, loadState: LoadState) {
		holder.onBindLoadState(loadState)
	}

	inner class ListLoadStateViewHolder(private val binding: ViewholderLoadStateBinding) :
		RecyclerView.ViewHolder(binding.root) {

		init {
			binding.btnRetry.setOnClickListener { retry.invoke() }
		}

		fun onBindLoadState(loadState: LoadState) = with(binding) {
			pbLoading.isVisible = loadState is LoadState.Loading
			txvError.isVisible = loadState !is LoadState.Loading
			btnRetry.isVisible = loadState !is LoadState.Loading
			if (loadState is LoadState.Error) {
				txvError.text = loadState.error.message
			}
		}
	}
}