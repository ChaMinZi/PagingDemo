package com.example.pagingdemo.models

sealed class ItemModel {
    data class ContentItem(val content: Content) : ItemModel()
    data class HeaderItem(val string: String) : ItemModel()
}