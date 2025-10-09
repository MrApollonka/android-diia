package ua.gov.diia.ps_criminal_cert.models.enums

enum class CriminalCertApplicationInfoNextStep(val code: String) {
    CONFIRMATION("confirmation"),
    BIRTH_PLACE("birth_place"),
    CITIZENSHIP("citizenship"),
    FORMAT_EXTRACT("format_extract"),
    CONTACTS("contacts"),
    POST_OFFICE("post_office_address_scheme"),
    COURIER("courier_address_scheme"),
    TYPE_DELIVERY("type_delivery");
}