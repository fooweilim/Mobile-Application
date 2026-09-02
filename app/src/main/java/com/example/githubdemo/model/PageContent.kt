package com.example.githubdemo.model

data class PageContent(
    val eyebrow: String,
    val title: String,
    val subtitle: String,
    val searchPlaceholder: String,
    val sectionTitle: String,
    val features: List<PageFeature>
)