package com.techeaz.support.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techeaz.support.data.QuickAction
import com.techeaz.support.databinding.ItemQuickActionBinding

/**
 * RecyclerView adapter for displaying quick action buttons
 */
class QuickActionAdapter(
    private val onActionClick: (QuickAction) -> Unit
) : ListAdapter<QuickAction, QuickActionAdapter.QuickActionViewHolder>(QuickActionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickActionViewHolder {
        val binding = ItemQuickActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuickActionViewHolder(binding, onActionClick)
    }

    override fun onBindViewHolder(holder: QuickActionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QuickActionViewHolder(
        private val binding: ItemQuickActionBinding,
        private val onActionClick: (QuickAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quickAction: QuickAction) {
            binding.tvActionTitle.text = quickAction.title
            binding.tvActionDescription.text = quickAction.description
            binding.tvActionIcon.text = quickAction.icon
            
            binding.root.setOnClickListener {
                onActionClick(quickAction)
            }
        }
    }

    class QuickActionDiffCallback : DiffUtil.ItemCallback<QuickAction>() {
        override fun areItemsTheSame(oldItem: QuickAction, newItem: QuickAction): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: QuickAction, newItem: QuickAction): Boolean {
            return oldItem == newItem
        }
    }
}