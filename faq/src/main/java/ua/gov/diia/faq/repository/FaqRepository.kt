package ua.gov.diia.faq.repository

import kotlinx.coroutines.flow.StateFlow
import ua.gov.diia.faq.model.CategoryInfo
import ua.gov.diia.faq.model.CategoryItem
import ua.gov.diia.faq.model.Faq

interface FaqRepository {

    val data: StateFlow<Faq?>

    suspend fun load()

    fun getCategoryItem(faq: Faq, code: String): CategoryItem?

    fun getCategoriesInfo(faq: Faq, code: String): List<CategoryInfo>

    fun clear()

}