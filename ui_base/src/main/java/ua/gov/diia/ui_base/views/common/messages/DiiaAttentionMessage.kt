package ua.gov.diia.ui_base.views.common.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.setupHtmlParameters
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.extensions.validateString
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled

class DiiaAttentionMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val infoTitle: TextView
    private val infoText: TextView
    private val infoLink: TextView
    private val emojiText: TextView

    init {
        inflate(context, R.layout.view_attantion_message, this)

        infoTitle = findViewById(R.id.info_title)
        infoText = findViewById(R.id.info_text)
        infoLink = findViewById(R.id.info_link)
        emojiText = findViewById(R.id.emoji)

        context.isTalkBackEnabled()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaAttentionMessage,
            defStyleAttr,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaAttentionMessage_msgTitle).run(::setMsgTitle)
                getString(R.styleable.DiiaAttentionMessage_msgText).run(::setMsgText)
                getString(R.styleable.DiiaAttentionMessage_msgEmoji).run(::setMsgEmoji)
                setBackgroundResource(R.drawable.back_penalties_paid)
            } finally {
                recycle()
            }
        }
    }

    fun setMsgTitle(message: String?) {
        message.validateString { string ->
            infoTitle.visibility = View.VISIBLE
            infoTitle.text = string
        }
        updateCombinedContentDescription()
    }

    fun setMsgTitle(@StringRes message: Int?) {
        message.validateResource { res ->
            infoTitle.visibility = View.VISIBLE
            infoTitle.text = context.getString(res)
        }
        updateCombinedContentDescription()
    }

    fun setMsgText(message: String?) {
        message.validateString { string ->
            infoText.visibility = VISIBLE
            infoText.text = string
        }
        updateCombinedContentDescription()
    }

    fun setMsgText(@StringRes message: Int?) {
        message.validateResource { res ->
            infoText.visibility = VISIBLE
            infoText.text = context.getString(res)
        }
        updateCombinedContentDescription()
    }

    fun setMsgEmoji(emoji: String?) {
        emoji.validateString { string -> emojiText.text = string }
    }

    fun setMsgEmoji(@StringRes emoji: Int?) {
        emoji.validateResource { res -> emojiText.text = context.getString(res) }
    }

    private fun updateCombinedContentDescription() {
        val parts = listOfNotNull(
            infoTitle.text?.takeIf { infoTitle.isVisible },
            infoText.text?.takeIf { infoText.isVisible }
        )

        contentDescription = parts.joinToString(separator = ". ")
        isFocusable = true
        isFocusableInTouchMode = true
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
    }
}

fun DiiaAttentionMessage.setupHtmlParameters(
    displayText: String?,
    metadata: List<TextParameter>?,
    onLinkClicked: ((url: String) -> Unit)?
) {
    val infoText = findViewById<TextView>(R.id.info_text)
    val infoLink = findViewById<TextView>(R.id.info_link)

    val isValid = displayText.isStringValid() && !metadata.isNullOrEmpty()
    val isTalkBackEnabled = context.isTalkBackEnabled()

    if (isValid) {
        val param = metadata?.first()
        val linkPlaceholder = param?.data?.name.let { "{$it}" }

        if (isTalkBackEnabled) {
            val textWithoutLink = displayText
                ?.replace(linkPlaceholder, "")
                ?.removeSuffix(".")
                ?.trim()

            infoText.visibility = View.VISIBLE
            infoText.text = textWithoutLink
            infoLink.visibility = View.VISIBLE
            infoLink.setupHtmlParameters(linkPlaceholder, metadata, onLinkClicked)
        } else {
            infoText.visibility = View.VISIBLE
            infoText.setupHtmlParameters(displayText, metadata, onLinkClicked)
            infoLink.visibility = View.GONE
        }
    } else {
        infoText.visibility = View.VISIBLE
        infoText.text = displayText
        infoLink.visibility = View.GONE
    }
}