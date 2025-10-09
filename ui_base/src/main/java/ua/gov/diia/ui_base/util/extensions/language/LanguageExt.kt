package ua.gov.diia.ui_base.util.extensions.language

fun String.detectLanguageCode(): String {
    val hasCyrillic = this.any { it in '\u0400'..'\u04FF' }
    val hasLatin = this.any { it in 'A'..'Z' || it in 'a'..'z' }

    return when {
        hasCyrillic && !hasLatin -> "uk"
        hasLatin && !hasCyrillic -> "en"
        else -> "uk"
    }
}