package ua.gov.diia.ps_criminal_cert.models.enums

enum class RepeatedDeliveryNextStep(val code: String) {
    REPEATED_DELIVERY_TYPE("type_repeated_delivery"),
    POST_OFFICE_ADDRESS_SCHEME("post_office_address_scheme"),
    COURIER_ADDRESS_SCHEME("courier_address_scheme"),
    CONTACTS_REPEATED_DELIVERY("contacts_repeated_delivery");
}