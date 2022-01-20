package com.example.pagingdemo.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class MyAutoArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                notifyDataSetChanged()
            }
        }
    }
}