package ua.gov.diia.faq.model

data class CategoryInfo(
    val parentName: String,
    val name: String,
    val code: String,
    val isCategoryGroup: Boolean
)