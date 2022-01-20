package com.example.pagingdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import com.example.pagingdemo.R
import com.example.pagingdemo.api.Network
import com.example.pagingdemo.database.getDatabase
import com.example.pagingdemo.databinding.FragmentContentListBinding
import com.example.pagingdemo.models.Content
import com.example.pagingdemo.repository.KakaoRepository
import com.example.pagingdemo.repository.KeywordRepository
import com.example.pagingdemo.viewmodels.ContentListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContentListFragment : Fragment() {

    private lateinit var binding: FragmentContentListBinding

    //    private val contentListViewModel by viewModels<ContentListViewModel>()
    private val contentViewModel by viewModels<ContentListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ContentListViewModel::class.java)) {
                    return ContentListViewModel(
                        KakaoRepository(Network.retrofit),
                        KeywordRepository(getDatabase(requireContext()))
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    private val spinnerAdapter = object : AdapterView.OnItemSelectedListener {
        var itemSelected: Int = 0

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, value: Int, p3: Long) {
            itemSelected = value
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            itemSelected = 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_content_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = contentViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 이동
        val adapter = ContentAdapter(
            ContentClickListener {
                binding.root.findNavController().navigate(
                    ContentListFragmentDirections.actionContentListToContent()
                )
            },
            FilterClickListener {
                Toast.makeText(
                    requireContext(),
                    "${spinnerAdapter.itemSelected} filter btn clicked",
                    Toast.LENGTH_SHORT
                ).show()
            },
            spinnerAdapter
        )
        binding.contentListRv.adapter = adapter

        contentViewModel.isSubmit.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                loadList(adapter)
            }
        }

        // App Bar
        val navController = findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.contentListToolbar.setupWithNavController(navController, appBarConfiguration)

        updateAdapter(emptyList())
        contentViewModel.keywordList.observe(viewLifecycleOwner) { keywords ->
            if (!keywords.isNullOrEmpty()) {
                updateAdapter(keywords)
            }
        }
    }

    private fun updateAdapter(items: List<String>) {
        val autoCompleteAdapter =
            MyAutoArrayAdapter(requireContext(), R.layout.item_drop_down, items)
        binding.mainAutoCompleteTv.apply {
            setAdapter(autoCompleteAdapter)
            setOnFocusChangeListener { _, focus ->
                showDropDown()
            }
        }
    }

    private suspend fun loadList(adapter: ContentAdapter) {
        contentViewModel.submitQuery.value?.let { query ->
            when (spinnerAdapter.itemSelected) {
                0 -> {
                    contentViewModel.searchBlog(query).collectLatest {
                        adapter.submitHeaderAndList(null, it)
                    }
                }
                1 -> {
                    contentViewModel.searchBlog(query).collectLatest {
                        adapter.submitHeaderAndList(null, it)
                    }
                }
                2 -> {
                    contentViewModel.searchCafe(query).collectLatest {
                        adapter.submitHeaderAndList(null, it)
                    }
                }
            }
        }
    }
}