package ua.gov.diia.faq.model

enum class FaqCategoryCode(val code: String) {
    ROOT_GROUP_NAME("all"),
    DRIVER_LICENCE("driverLicense"),
    VEHICLE_LICENCE("vehicleLicense"),
    ID_CARD("idCardAndForeignPassport"),
    FOREIGN_PASSPORT("idCardAndForeignPassport"),
    STUDENT_CARD("studentCard"),
    TAXPAYER_CARD("taxpayerCard"),
    RIDP("referenceInternallyDisplacedPerson"),
    BIRTHDAY_CERT("birthCertificate"),
    VACCINATION_CERTIFICATE("vaccinationCertificate"),
    E_DOCUMENT("uID"),
    RESIDENT_PERMIT("residencePermit"),
    OFFICIAL_SERVICES("ServicesStateServant"),
    EDUCATION_CARD("educationDocument"),
    VETERAN_CARD("veteranCertificate"),
    EDUCATIONAL_CERT("educationalCertificate")
}