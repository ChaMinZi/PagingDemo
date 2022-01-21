package com.example.pagingdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
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
import com.example.pagingdemo.R
import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.database.SearchDatabase
import com.example.pagingdemo.databinding.FragmentContentListBinding
import com.example.pagingdemo.repository.KakaoRepository
import com.example.pagingdemo.repository.KeywordRepository
import com.example.pagingdemo.viewmodels.ContentListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContentListFragment : Fragment() {

    private lateinit var binding: FragmentContentListBinding
    
    @Inject
    lateinit var kakaoRepository: KakaoRepository

    @Inject
    lateinit var keywordRepository: KeywordRepository

    private val contentViewModel by viewModels<ContentListViewModel>()

    private val spinnerAdapter = object : AdapterView.OnItemSelectedListener {
        var itemSelected: Int = 0

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, value: Int, p3: Long) {
            lifecycleScope.launch {
                itemSelected = value
                loadList()
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            itemSelected = 0
        }
    }

    private val recyclerAdapter by lazy {
        ContentAdapter(
            ContentClickListener {
                binding.root.findNavController().navigate(
                    ContentListFragmentDirections.actionContentListToContent(it)
                )
            },
            FilterClickListener {
                val typeArray = resources.getStringArray(R.array.list_type_array)
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Select")
                    .setSingleChoiceItems(typeArray, -1) { dialog, i ->

                    }
                    .setPositiveButton("ok") { dialog, id ->

                    }
                    .setNegativeButton("cancel") { dialog, id ->

                    }
                builder.show()
            },
            spinnerAdapter
        )
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
        binding.contentListRv.adapter = recyclerAdapter

        contentViewModel.isSubmit.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                loadList()
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

    private suspend fun loadList() {
        contentViewModel.submitQuery.value?.let { query ->
            when (spinnerAdapter.itemSelected) {
                0 -> {
                    contentViewModel.searchBlog(query).collectLatest {
//                        recyclerAdapter.submitHeaderAndList(it)
                        recyclerAdapter.submitData(it)
                    }
                }
                1 -> {
                    contentViewModel.searchBlog(query).collectLatest {
//                        recyclerAdapter.submitHeaderAndList(it)
                        recyclerAdapter.submitData(it)
                    }
                }
                2 -> {
                    contentViewModel.searchCafe(query).collectLatest {
//                        recyclerAdapter.submitHeaderAndList(it)
                        recyclerAdapter.submitData(it)
                    }
                }
            }
        }
    }
}