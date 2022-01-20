package com.example.pagingdemo.utils

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pagingdemo.R

@BindingAdapter("imageUrl")
fun bindImageFromUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide
            .with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_image)
            .into(imageView)
    }
}

@BindingAdapter("text")
fun TextView.bindDateFormat(string: String?) {
    text = string?.let {
        convertStringToDateString(string)
    }
}

/**
 * https://velog.io/@vov3616/Spinner%EB%A5%BC-databinding-%ED%95%B4%EB%B3%B4%EC%9E%90
 **/
//@BindingAdapter("selectedValue")
//fun setSelectedValue(spinner: Spinner, selectedValue: Int) {
//    spinner.adapter?.run {
////        val position =
////            (adapter as ArrayAdapter<Any>).getPosition(selectedValue)
//        spinner.setSelection(selectedValue, false)
//    }
//}
//
//@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
//fun getSelectedValue(spinner: Spinner): Int {
//    return spinner.selectedItemPosition
//}
//
//@BindingAdapter("selectedValueAttrChanged")
//fun setInverseBindingListener(
//    spinner: Spinner,
//    inverseBindingListener: InverseBindingListener
//) {
//    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(
//            parent: AdapterView<*>,
//            view: View,
//            position: Int,
//            id: Long
//        ) {
//            inverseBindingListener.onChange()
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>) {}
//    }
//}
