package ua.gov.diia.opensource.helper

import ua.gov.diia.faq.helper.FaqHelper

class FaqHelperImpl : FaqHelper {

    override fun getFaqCategoryCodeByDocumentCode(code: String): String {
        return code
    }

}