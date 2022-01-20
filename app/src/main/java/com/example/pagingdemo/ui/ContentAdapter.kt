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
import com.example.pagingdemo.models.ItemModel
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
    PagingDataAdapter<ItemModel, RecyclerView.ViewHolder>(ContentDiffCallback) {

    class ItemViewHolder private constructor(private val binding: ItemContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: Content, clickListener: ContentClickListener) {
            binding.item = content
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
        return if (position == VIEW_TYPE_HEADER) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemModel = getItem(position)
        itemModel?.let { itemModel ->
            when (itemModel) {
                is ItemModel.ContentItem -> (holder as ItemViewHolder).bind(
                    itemModel.content,
                    onClickListener
                )
                is ItemModel.HeaderItem -> (holder as HeaderViewHolder).bind(
                    filterClickListener,
                    spinnerAdapter
                )
            }
        }
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitHeaderAndList(pagingData: PagingData<ItemModel>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitData(pagingData)
            }
        }
    }
}

object ContentDiffCallback : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        val isSameContentItem =
            (oldItem is ItemModel.ContentItem && newItem is ItemModel.ContentItem && oldItem.content == newItem.content)
        val isSameHeaderItem =
            (oldItem is ItemModel.HeaderItem && newItem is ItemModel.HeaderItem && oldItem.string == newItem.string)
        return isSameContentItem || isSameHeaderItem
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem == newItem
    }
}

class ContentClickListener(private val clickListener: (item: Content) -> Unit) {
    fun onClick(item: Content) = clickListener(item)
}

class FilterClickListener(private val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}