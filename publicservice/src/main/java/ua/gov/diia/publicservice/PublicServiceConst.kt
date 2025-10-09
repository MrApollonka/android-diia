package ua.gov.diia.publicservice

object PublicServiceConst {
    internal const val PS_ENEMY = "enemy"

    const val PS_MILITARY_DISABILITY_STATUS = "militaryDisabilityStatus"
    const val PS_VETERAN_ASSISTANT_SEARCH = "veteranAssistantSearch"
    const val PS_WORKPLACE_COMPENSATION = "workplaceCompensation"
    const val PS_VETERANS_FAMILIES_GRANT = "veteransFamiliesGrant"

    internal val PORTAL_SERVICES = listOf(
        PS_MILITARY_DISABILITY_STATUS,
        PS_WORKPLACE_COMPENSATION,
        PS_VETERANS_FAMILIES_GRANT
    )

    internal fun mapServiceCodeToPortalCode(serviceCode: String): String {
        return when (serviceCode) {
            PS_MILITARY_DISABILITY_STATUS -> "military-disability-status"
            PS_WORKPLACE_COMPENSATION -> "workplace-compensation"
            PS_VETERANS_FAMILIES_GRANT -> "veterans-families-grant"

            else -> serviceCode
        }
    }
}