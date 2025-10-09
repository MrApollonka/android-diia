package ua.gov.diia.core.util.decoder

interface Base64Wrapper {

    fun encode(data: ByteArray): ByteArray

    fun decode(data: ByteArray): ByteArray
}
