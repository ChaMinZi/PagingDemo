package com.example.pagingdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pagingdemo.databinding.FragmentWebBinding

class WebFragment : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // App Bar
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.webToolbar.setupWithNavController(navController, appBarConfiguration)
//        val content = mainViewModel.content.value
//
//        content?.let {
//            mainViewModel.updateActionBarTitle(it.title)
//            initWebView(it)
//        }
    }

//    private fun initWebView(content: Content) {
//        binding.urlWebView.apply {
//            settings.javaScriptEnabled = true
//            webViewClient = WebViewClient()
//            loadUrl(content.url)
//        }
//    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}