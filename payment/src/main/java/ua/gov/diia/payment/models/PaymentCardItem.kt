package ua.gov.diia.payment.models

interface PaymentCardItem<T>

data class SeeAllCardItem<T>(val moreCount: Int) : PaymentCardItem<T>