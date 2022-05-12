package com.example.sampleproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sampleproject.databinding.ItemUserBinding
import com.example.sampleproject.model.Data

class UserAdapter(diffCallback: DiffUtil.ItemCallback<Data>) :
    PagingDataAdapter<Data, UserAdapter.UserViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item)
    }

    object UserComparator : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    class UserViewHolder private constructor(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data?) {
            binding.userData = item
            Glide.with(binding.root.context).load(item?.avatar).into(binding.ivProfilePic)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }
}