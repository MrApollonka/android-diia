package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.core.models.common_compose.org.input.question_form.SelectorListWidgetOrgItem
import javax.inject.Inject


interface CriminalCertNationalitiesMapper {
    fun toNationalityList(items: List<SelectorListWidgetOrgItem>?): List<NationalityItem>
}

class CriminalCertNationalitiesMapperImpl @Inject constructor() :
    CriminalCertNationalitiesMapper {
    override fun toNationalityList(items: List<SelectorListWidgetOrgItem>?): List<NationalityItem> {
        val nationalityItems: MutableList<NationalityItem> = mutableListOf()
        items?.forEach {
            nationalityItems.add(
                NationalityItem(
                    componentId = it.listWidgetItemMlc?.componentId,
                    id = it.listWidgetItemMlc?.id,
                    label = it.listWidgetItemMlc?.label ?: "",
                    containerId = null
                )
            )
        }
        return nationalityItems
    }
}