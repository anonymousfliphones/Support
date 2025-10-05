package com.techeaz.support.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techeaz.support.R
import com.techeaz.support.data.Announcement
import com.techeaz.support.databinding.ItemAnnouncementBinding

/**
 * RecyclerView adapter for displaying announcements
 */
class AnnouncementAdapter : ListAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder>(AnnouncementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AnnouncementViewHolder(
        private val binding: ItemAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: Announcement) {
            binding.tvAnnouncementTitle.text = announcement.title
            binding.tvAnnouncementMessage.text = announcement.message
            binding.tvAnnouncementDate.text = "Posted: ${announcement.getFormattedDate()}"
            
            // Set icon and color based on announcement type
            val (icon, color) = when (announcement.type) {
                "warning" -> "⚠️" to ContextCompat.getColor(binding.root.context, R.color.warning)
                "error" -> "🚨" to ContextCompat.getColor(binding.root.context, R.color.error)
                "info" -> "ℹ️" to ContextCompat.getColor(binding.root.context, R.color.info)
                else -> "📢" to ContextCompat.getColor(binding.root.context, R.color.primary)
            }
            
            binding.tvAnnouncementIcon.text = icon
            binding.viewTypeIndicator.setBackgroundColor(color)
        }
    }

    class AnnouncementDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}