package com.example.pagingdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pagingdemo.R
import com.example.pagingdemo.databinding.FragmentContentBinding

class ContentFragment : Fragment() {

    private lateinit var binding: FragmentContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var args = ContentFragmentArgs.fromBundle(requireArguments()).content
        binding.item = args

        // App Bar
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.contentItemToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.contentItemToolbar.title = args.label

        // Web Url 이동
        binding.contentItemMoveBtn.setOnClickListener {
            binding.root.findNavController().navigate(
                ContentFragmentDirections.actionContentToWeb(args.title, args.url)
            )
        }
    }
}