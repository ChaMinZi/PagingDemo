package com.example.pagingdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingdemo.R
import com.example.pagingdemo.databinding.HeaderContentListBinding
import com.example.pagingdemo.databinding.ItemContentListBinding
import com.example.pagingdemo.models.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1

class ContentAdapter(
    private val onClickListener: ContentClickListener,
    private val filterClickListener: FilterClickListener,
    private val spinnerAdapter: AdapterView.OnItemSelectedListener
) :
    PagingDataAdapter<Content, RecyclerView.ViewHolder>(ContentDiffCallback()) {

    class ItemViewHolder private constructor(private val binding: ItemContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content, clickListener: ContentClickListener) {
            binding.content = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContentListBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder private constructor(private val binding: HeaderContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            filterClickListener: FilterClickListener,
            itemSelectedListener: AdapterView.OnItemSelectedListener
        ) {
            binding.filterClickListener = filterClickListener
            binding.headerSpinner.onItemSelectedListener = itemSelectedListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderContentListBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
        VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
        else -> throw IllegalStateException("Not Found ViewHolder Type $viewType")
    }

    override fun getItemViewType(position: Int): Int {
//        return if (position == VIEW_TYPE_HEADER) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(filterClickListener, spinnerAdapter)
            }
            is ItemViewHolder -> {
                val item = getItem(position)
                item?.let {
                    holder.bind(it, onClickListener)
                }
            }
        }
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitHeaderAndList(list: MutableList<Content>?, pagingData: PagingData<Content>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitData(pagingData)
            }
        }
    }
}

class ContentDiffCallback : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem.contents == newItem.contents
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }
}

class ContentClickListener(private val clickListener: (item: Content) -> Unit) {
    fun onClick(item: Content) = clickListener(item)
}

class FilterClickListener(private val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}