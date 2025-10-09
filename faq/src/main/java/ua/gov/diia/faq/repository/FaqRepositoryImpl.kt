package ua.gov.diia.faq.repository

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.util.extensions.date_time.getCurrentDateUtc
import ua.gov.diia.core.util.extensions.date_time.getUTCDate
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.faq.di.MoshiAdapterFaq
import ua.gov.diia.faq.model.CategoryGroup
import ua.gov.diia.faq.model.CategoryInfo
import ua.gov.diia.faq.model.CategoryItem
import ua.gov.diia.faq.model.Faq
import ua.gov.diia.faq.network.ApiFaq
import javax.inject.Inject

class FaqRepositoryImpl @Inject constructor(
    @GlobalActionNetworkState private val connectivityObserver: ConnectivityObserver,
    @AuthorizedClient private val apiFaq: ApiFaq,
    @MoshiAdapterFaq private val moshiAdapterFaq: JsonAdapter<Faq>,
    private val diiaStorage: DiiaStorage
) : FaqRepository {

    override val data = MutableStateFlow<Faq?>(null)

    override suspend fun load() {
        val cachedFaq = loadCachedData()

        if (connectivityObserver.isAvailable) {
            if (cachedFaq == null || isDateExpired(cachedFaq.expirationDate)) {
                apiFaq.getFaq().let { faq ->
                    data.update { faq }
                    cacheData(faq)
                }
            } else {
                data.update { cachedFaq }
            }
        } else {
            data.update { cachedFaq }
        }
    }

    override fun getCategoryItem(faq: Faq, code: String): CategoryItem? {
        return faq.categories.find { it.code == code }
    }

    override fun getCategoriesInfo(faq: Faq, code: String): List<CategoryInfo> {
        val result = mutableListOf<CategoryInfo>()

        findCategoryGroupByCode(faq, code)?.let { parentGroup ->
            val parentName = parentGroup.title

            parentGroup.categoriesGroups?.forEach { nestedCode ->
                val nestedGroup = findCategoryGroupByCode(faq, nestedCode)
                if (nestedGroup != null) {
                    result.add(
                        CategoryInfo(
                            parentName = parentName,
                            name = nestedGroup.title,
                            code = nestedGroup.code,
                            isCategoryGroup = true
                        )
                    )
                }
            }

            parentGroup.categories?.let { categoryCodes ->
                categoryCodes.forEach { categoryCode ->
                    val category = getCategoryItem(faq, categoryCode)
                    if (category != null) {
                        result.add(
                            CategoryInfo(
                                parentName = parentName,
                                name = category.name,
                                code = category.code,
                                isCategoryGroup = categoryCodes.size > 1
                            )
                        )
                    }
                }
            }
        }

        if (result.isEmpty()) {
            val category = getCategoryItem(faq, code)
            if (category != null) {
                result.add(
                    CategoryInfo(
                        parentName = category.name,
                        name = category.name,
                        code = category.code,
                        isCategoryGroup = false
                    )
                )
            }
        }

        return result
    }

    override fun clear() {
        data.update { null }
    }

    private fun isDateExpired(date: String?): Boolean {
        return date?.let { lDate -> getUTCDate(lDate)?.before(getCurrentDateUtc()) } ?: true
    }

    private fun cacheData(faq: Faq) {
        val jsonString = moshiAdapterFaq.toJson(faq)
        diiaStorage.set(Preferences.FaqsList, jsonString)
    }

    private fun loadCachedData(): Faq? = try {
        val faqs = diiaStorage.getString(Preferences.FaqsList, "")
        if (faqs.isNotEmpty()) {
            moshiAdapterFaq.fromJson(faqs)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }

    private fun findCategoryGroupByCode(faq: Faq, code: String): CategoryGroup? {
        return faq.categoriesGroups.find { it.code == code }
    }

}