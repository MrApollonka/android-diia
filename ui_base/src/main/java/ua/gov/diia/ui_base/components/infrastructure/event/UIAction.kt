package ua.gov.diia.ui_base.components.infrastructure.event

import ua.gov.diia.core.util.extensions.emptyDeque
import ua.gov.diia.core.util.extensions.push
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.state.UIState

//Use 'optionalId' only when 'data' field is already filled, but you need to identify view in group
//In case when you just need to send id to VM - send it as 'data' value
data class UIAction(
    val actionKey: String,
    val data: String? = null,
    val states: List<UIState> = emptyList(),
    val optionalId: String? = null,
    val optionalType: String? = null,
    val action: DataActionWrapper? = null,
    val actions: List<DataActionWrapper>? = null,
    val actionBundles: ArrayDeque<ActionBundle> = emptyDeque()
)

data class ActionBundle(
    val componentId: String?,
    val action: DataActionWrapper? = null
)

fun UIAction.pushBundle(
    componentId: String?,
    action: DataActionWrapper? = null
) = this.copy(
    actionBundles = actionBundles.push(
        element = ActionBundle(
            componentId = componentId,
            action = action
        )
    )
)

fun UIAction.getValidationStateOrNull(): UIState.Validation? {
    return if (this.states.isNotEmpty()) {
        this.states.firstOrNull {
            it is UIState.Validation
        } as UIState.Validation
    } else {
        null
    }
}

fun UIAction.getSelectionStateOrNull(): UIState.Selection? {
    return if (this.states.isNotEmpty()) {
        this.states.firstOrNull {
            it is UIState.Selection
        } as UIState.Selection
    } else {
        null
    }
}

fun UIAction.getExpandStateOrNull(): UIState.Expand? {
    return if (this.states.isNotEmpty()) {
        this.states.firstOrNull {
            it is UIState.Expand
        } as UIState.Expand
    } else {
        null
    }
}

fun List<UIState>.getSelectionStateOrNull(): UIState.Selection? {
    return if (this.isNotEmpty()) {
        this.firstOrNull {
            it is UIState.Selection
        } as UIState.Selection
    } else {
        null
    }
}