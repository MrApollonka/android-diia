package ua.gov.diia.ui_base.components.organism.document

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.image.BlurBitmap
import ua.gov.diia.ui_base.components.infrastructure.utils.isTalkBackEnabled
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroup
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupData
import ua.gov.diia.ui_base.components.organism.pager.DocType.E_DOCUMENT
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha40
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha25
import ua.gov.diia.ui_base.models.orientation.Orientation

@Composable
fun DocCodeOrg(
    modifier: Modifier = Modifier,
    data: DocCodeOrgData,
    orientation: Orientation = Orientation.Portrait,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    docType: String? = null,
    onUIAction: (UIAction) -> Unit
) {
    when (orientation) {
        Orientation.Portrait -> DocCodeOrgPortrait(modifier, data, progressIndicator, docType, onUIAction)
        Orientation.Landscape -> DocCodeOrgLandscape(modifier, data, progressIndicator, docType, onUIAction)
    }
}

@Composable
fun DocCodeOrgPortrait(
    modifier: Modifier,
    data: DocCodeOrgData,
    progressIndicator: Pair<String, Boolean>,
    docType: String? = null,
    onUIAction: (UIAction) -> Unit
) {
    var docBarcodeExpired by remember { mutableStateOf(data.expired) }
    val context = LocalContext.current
    val view = LocalView.current
    val focusRequesterTextCodeExpired = remember { FocusRequester() }

    val isTalkBackEnabled = context.isTalkBackEnabled()
    var shouldAnnounce by remember { mutableStateOf(false) }
    val showLoader = data.actionKey == progressIndicator.first && progressIndicator.second

    if (isTalkBackEnabled) {
        LaunchedEffect(key1 = docBarcodeExpired) {
            if (docBarcodeExpired) {
                view.announceForAccessibility(context.getString(R.string.accessibility_qr_expired))
                focusRequesterTextCodeExpired.requestFocus()
            }
        }

        LaunchedEffect(key1 = showLoader) {
            if (!showLoader) {
                shouldAnnounce = true
            }
        }

        LaunchedEffect(key1 = shouldAnnounce) {
            if (shouldAnnounce && data.exception == null) {
                val message = when {
                    docType == E_DOCUMENT -> {
                        context.getString(R.string.accessibility_code_displayed_for_verification)
                    }

                    else -> {
                        val seconds = data.timerTime ?: 180
                        val min = seconds / 60
                        val sec = seconds % 60

                        val timerMessage = context.getString(
                            R.string.accessibility_code_will_be_valid_x_min,
                            min, sec
                        )

                        context.getString(R.string.accessibility_code_displayed_for_verification) +
                                timerMessage
                    }
                }

                view.announceForAccessibility(message)
                shouldAnnounce = false
            }
        }
    }

    Column(
        modifier = modifier
            .aspectRatio(if (!data.isStack) 0.7F else 0.684F),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7F)
                .background(color = White, shape = RoundedCornerShape(24.dp))
        ) {
            if (data.actionKey == progressIndicator.first && progressIndicator.second) {
                TridentLoaderMolecule()
            } else {
                ConstraintLayout(
                    modifier = Modifier
                        .aspectRatio(0.7F)
                        .padding(top = 24.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)
                ) {
                    val (timerText, expiredContent, qrImage, ean13Image, toggleGroup, exceptionContent) = createRefs()

                    if (data.exception == null) {
                        val topMargin =
                            if (data.toggle.ean13.selectionState == UIState.Selection.Selected)
                                58.dp
                            else 8.dp
                        data.timerText?.let {
                            TimerText(
                                text = data.timerText,
                                totalSeconds = data.timerTime ?: 180,
                                units = if (data.localization == Localization.ua) "хв" else "min",
                                modifier = Modifier.constrainAs(timerText) {
                                    top.linkTo(parent.top, margin = topMargin)
                                }
                            ) {
                                docBarcodeExpired = true
                            }
                        }
                        if (docBarcodeExpired) {
                            Box(
                                modifier = Modifier
                                    .constrainAs(expiredContent) {
                                        top.linkTo(
                                            timerText.bottom,
                                            margin = if (data.showToggle) 16.dp else 0.dp
                                        )
                                        if (!data.showToggle) {
                                            bottom.linkTo(parent.bottom)
                                        }
                                    },
                            ) {
                                if (data.toggle.qr.selectionState == UIState.Selection.Selected) {
                                    data.qrBitmap?.let {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val originalQRBitmap =
                                                it.copy(it.config ?: Bitmap.Config.ARGB_8888, true)

                                            BlurBitmap(
                                                bitmap = originalQRBitmap,
                                                width = 231.dp,
                                                height = 231.dp
                                            )
                                        }
                                    }
                                }
                                if (data.toggle.ean13.selectionState == UIState.Selection.Selected) {
                                    data.ean13Bitmap?.let {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val originalEan13Bitmap =
                                                it.copy(it.config ?: Bitmap.Config.ARGB_8888, true)

                                            BlurBitmap(
                                                bitmap = originalEan13Bitmap,
                                                width = 247.dp,
                                                height = 100.dp
                                            )
                                        }
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "☝",
                                        style = TextStyle(
                                            fontSize = 32.sp,
                                            lineHeight = 36.sp
                                        ),
                                        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

                                        )
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 16.dp)
                                            .focusRequester(focusRequesterTextCodeExpired)
                                            .focusable(),
                                        text = if (data.localization == Localization.eng) {
                                            "The code has expired"
                                        } else {
                                            "Час дії коду закінчився"
                                        },
                                        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp
                                        )
                                    )

                                    BtnStrokeAdditionalAtm(
                                        modifier = Modifier.padding(top = 24.dp),
                                        data = ButtonStrokeAdditionalAtomData(
                                            title = if (data.localization == Localization.eng) {
                                                UiText.DynamicString("Update the code")

                                            } else {
                                                UiText.DynamicString("Оновити код")
                                            }
                                        ),
                                        onUIAction = {
                                            onUIAction(
                                                UIAction(
                                                    actionKey = UIActionKeysCompose.REFRESH_BUTTON
                                                )
                                            )
                                            docBarcodeExpired = false

                                        }
                                    )
                                }
                            }
                        } else {
                            if (data.toggle.qr.selectionState == UIState.Selection.Selected) {
                                data.qrBitmap?.let {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = if (data.showToggle || data.timerText != null) 16.dp else 0.dp)
                                            .fillMaxWidth()
                                            .constrainAs(qrImage) {
                                                if (data.showToggle) {
                                                    top.linkTo(timerText.bottom)

                                                } else {
                                                    top.linkTo(parent.top)
                                                    bottom.linkTo(parent.bottom)

                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f),
                                            bitmap = data.qrBitmap.asImageBitmap(),
                                            contentScale = ContentScale.Fit,
                                            contentDescription = stringResource(id = R.string.qr_code)
                                        )
                                    }
                                }
                            }
                            if (data.toggle.ean13.selectionState == UIState.Selection.Selected) {
                                data.ean13Bitmap?.let {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 16.dp)
                                            .fillMaxWidth()
                                            .constrainAs(ean13Image) {
                                                top.linkTo(timerText.bottom)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                modifier = Modifier.size(
                                                    width = 247.dp,
                                                    height = 100.dp
                                                ),
                                                bitmap = data.ean13Bitmap.asImageBitmap(),
                                                contentDescription = stringResource(id = R.string.barcode),
                                                contentScale = ContentScale.FillBounds
                                            )
                                            data.eanCode?.let {
                                                Text(
                                                    text = formatNumber(data.eanCode),
                                                    modifier = Modifier.padding(
                                                        top = 12.dp,
                                                        bottom = 32.dp
                                                    ),
                                                    style = TextStyle(
                                                        fontSize = 14.sp,
                                                        lineHeight = 16.sp,
                                                        letterSpacing = 4.sp
                                                    ),
                                                    fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                                    color = Black,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (data.showToggle) {
                            ToggleButtonGroup(
                                modifier = Modifier
                                    .constrainAs(toggleGroup) {
                                        bottom.linkTo(parent.bottom)
                                    },
                                data = data.toggle,
                                onUIAction = onUIAction
                            )
                        }
                    } else {
                        val text = with(data) {
                            when {
                                localization == Localization.eng && noRegistry == null ->
                                    "Verification codes failed to upload"

                                localization == Localization.eng && noRegistry != null ->
                                    "No registry access"

                                localization != Localization.eng && noRegistry == null ->
                                    "Коди для перевірки не завантажено"

                                else ->
                                    "Немає доступу до реєстру"
                            }
                        }

                        LaunchedEffect(key1 = Unit) {
                            view.announceForAccessibility(text)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .constrainAs(exceptionContent) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (data.noRegistry == null) "☝" else "\uD83D\uDE14",
                                style = TextStyle(
                                    fontSize = 36.sp,
                                    lineHeight = 40.sp
                                ),
                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

                                )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = text,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            BtnStrokeAdditionalAtm(
                                modifier = Modifier,
                                data = ButtonStrokeAdditionalAtomData(
                                    title = if (data.localization == Localization.eng) {
                                        UiText.DynamicString("Try again")
                                    } else {
                                        UiText.DynamicString("Спробувати ще раз")
                                    }
                                ),
                                onUIAction = {
                                    onUIAction(
                                        UIAction(
                                            actionKey = UIActionKeysCompose.REFRESH_BUTTON
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        if (data.isStack) {
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .background(
                        color = WhiteAlpha25,
                        shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                    )
            )
        }
    }
}

@Composable
fun DocCodeOrgLandscape(
    modifier: Modifier,
    data: DocCodeOrgData,
    progressIndicator: Pair<String, Boolean>,
    docType: String? = null,
    onUIAction: (UIAction) -> Unit
) {
    var docBarcodeExpired by remember { mutableStateOf(data.expired) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White, shape = RoundedCornerShape(24.dp)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (data.actionKey == progressIndicator.first && progressIndicator.second) {
                TridentLoaderMolecule()
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (data.exception != null) {
                        CodeExpiredOrg(data, onUIAction)
                    } else {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(if (data.showToggle) 0.5f else 1f)
                                    .fillMaxHeight()
                                    .padding(
                                        top = 24.dp,
                                        start = 32.dp,
                                        end = 32.dp,
                                        bottom = 24.dp
                                    ),
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (docBarcodeExpired) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (data.toggle.qr.selectionState == UIState.Selection.Selected) {
                                            data.qrBitmap?.let {
                                                BoxWithConstraints(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    val originalQRBitmap =
                                                        it.copy(
                                                            it.config ?: Bitmap.Config.ARGB_8888,
                                                            true
                                                        )
                                                    val size = maxHeight
                                                    BlurBitmap(
                                                        bitmap = originalQRBitmap,
                                                        width = size,
                                                        height = size
                                                    )
                                                }
                                            }
                                        }
                                        if (data.toggle.ean13.selectionState == UIState.Selection.Selected) {
                                            data.ean13Bitmap?.let {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    val originalEan13Bitmap = it.copy(
                                                        it.config ?: Bitmap.Config.ARGB_8888,
                                                        true
                                                    )

                                                    BlurBitmap(
                                                        bitmap = originalEan13Bitmap,
                                                        width = 188.dp,
                                                        height = 75.dp
                                                    )
                                                }
                                            }
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "☝",
                                                style = TextStyle(
                                                    fontSize = 32.sp,
                                                    lineHeight = 36.sp
                                                ),
                                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

                                                )
                                            Text(
                                                modifier = Modifier.padding(top = 16.dp),
                                                text = if (data.localization == Localization.eng) {
                                                    "The code has expired"
                                                } else {
                                                    "Час дії коду закінчився"
                                                },
                                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                                textAlign = TextAlign.Center,
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    lineHeight = 24.sp
                                                )
                                            )

                                            BtnStrokeAdditionalAtm(
                                                modifier = Modifier.padding(top = 24.dp),
                                                data = ButtonStrokeAdditionalAtomData(
                                                    title = if (data.localization == Localization.eng) {
                                                        UiText.DynamicString("Update the code")
                                                    } else {
                                                        UiText.DynamicString("Оновити код")
                                                    }
                                                ),
                                                onUIAction = {
                                                    onUIAction(
                                                        UIAction(
                                                            actionKey = UIActionKeysCompose.REFRESH_BUTTON
                                                        )
                                                    )
                                                    docBarcodeExpired = false
                                                }
                                            )
                                        }
                                    }
                                } else {
                                    data.timerText?.let {
                                        TimerText(
                                            text = data.timerText,
                                            totalSeconds = data.timerTime ?: 180,
                                            units = if (data.localization == Localization.ua) "хв" else "min",
                                            modifier = Modifier
                                        ) {
                                            docBarcodeExpired = true
                                        }
                                    }
                                    if (data.toggle.qr.selectionState == UIState.Selection.Selected) {
                                        data.qrBitmap?.let {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = if (data.timerText != null) 8.dp else 0.dp)
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    modifier = Modifier.size(
                                                        width = 168.dp,
                                                        height = 168.dp
                                                    ),
                                                    bitmap = data.qrBitmap.asImageBitmap(),
                                                    contentScale = ContentScale.Fit,
                                                    contentDescription = stringResource(id = R.string.qr_code)
                                                )
                                            }
                                        }
                                    }
                                    if (data.toggle.ean13.selectionState == UIState.Selection.Selected) {
                                        data.ean13Bitmap?.let {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 16.dp)
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        modifier = Modifier.size(
                                                            width = 188.dp,
                                                            height = 75.dp
                                                        ),
                                                        bitmap = data.ean13Bitmap.asImageBitmap(),
                                                        contentDescription = stringResource(id = R.string.qr_code),
                                                        contentScale = ContentScale.FillBounds
                                                    )
                                                    data.eanCode?.let {
                                                        Text(
                                                            text = formatNumber(data.eanCode),
                                                            modifier = Modifier.padding(top = 12.dp),
                                                            style = TextStyle(
                                                                fontSize = 14.sp,
                                                                lineHeight = 16.sp,
                                                                letterSpacing = 4.sp
                                                            ),
                                                            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                                            color = Black,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (data.showToggle) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ToggleButtonGroup(
                                        modifier = Modifier,
                                        data = data.toggle,
                                        onUIAction = onUIAction
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (data.isStack) {
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .background(
                        color = WhiteAlpha25,
                        shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                    )
            )
        }
    }
}

@Composable
private fun CodeExpiredOrg(data: DocCodeOrgData, onUIAction: (UIAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (data.noRegistry == null) "☝" else "\uD83D\uDE14",
            style = TextStyle(
                fontSize = 36.sp,
                lineHeight = 40.sp
            ),
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

            )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = if (data.localization == Localization.eng) {
                if (data.noRegistry == null) "Verification codes failed to upload" else "No registry access"
            } else {
                if (data.noRegistry == null) "Коди для перевірки не завантажено" else "Немає доступу до реєстру"
            },
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        BtnStrokeAdditionalAtm(
            modifier = Modifier,
            data = ButtonStrokeAdditionalAtomData(
                title = if (data.localization == Localization.eng) {
                    UiText.DynamicString("Try again")
                } else {
                    UiText.DynamicString("Спробувати ще раз")
                }
            ),
            onUIAction = {
                onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.REFRESH_BUTTON
                    )
                )
            }
        )
    }
}

enum class Localization {
    ua, eng
}

data class DocCodeOrgData(
    val actionKey: String = UIActionKeysCompose.DOC_CODE_ORG_DATA,
    val localization: Localization,
    val toggle: ToggleButtonGroupData,
    val qrBitmap: Bitmap? = null,
    val ean13Bitmap: Bitmap? = null,
    val eanCode: String? = null,
    val timerText: String? = null,
    val timerTime: Int? = 180,
    val exception: Exception? = null,
    val expired: Boolean = false,
    val showToggle: Boolean,
    val isStack: Boolean,
    val noRegistry: Int? = null,
    val docType: String? = null,
) : UIElementData {
    fun onToggleClick(toggleId: String?): DocCodeOrgData {
        if (toggleId == null) return this
        return this.copy(
            toggle = toggle.onToggleClicked(toggleId)
        )
    }
}

fun formatNumber(eanCode: String): String {
    val spacedEanCode = StringBuilder(eanCode)
    spacedEanCode.insert(4, " ")
    spacedEanCode.insert(9, " ")
    return spacedEanCode.toString()
}

@Preview
@Composable
fun DocCodeOrgPreview() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            timerTime = 120,
            qrBitmap = qrBm,
            showToggle = true,
            isStack = true
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocCodeOrgLandscapePreview() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            showToggle = true,
            isStack = true
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, orientation = Orientation.Landscape, data = stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview
@Composable
fun DocCodeOrgExpiredPreview() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            timerText = "00:00",
            timerTime = 180,
            showToggle = false,
            expired = true,
            isStack = true
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun DocCodeOrgExpiredLandscapePreview() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            timerText = "00:00",
            timerTime = 120,
            showToggle = false,
            expired = true,
            isStack = true
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, data = stateT.value, orientation = Orientation.Landscape) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview
@Composable
fun DocCodeOrgPreviewLoading() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            showToggle = true,
            isStack = false
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value, progressIndicator = Pair(data.actionKey, true)) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview
@Composable
fun DocCodeOrgPreviewWithHttpExeption() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data = DocCodeOrgData(
        localization = Localization.eng,
        toggle = state.value,
        qrBitmap = qrBm,
        exception = java.lang.Exception(),
        showToggle = true,
        isStack = false
    )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Composable
fun TimerText(
    text: String?,
    units: String,
    totalSeconds: Int,
    modifier: Modifier,
    onTimeOver: () -> Unit
) {
    val density = LocalDensity.current

    var minutes by remember { mutableIntStateOf(totalSeconds / 60) }
    var seconds by remember { mutableIntStateOf(totalSeconds % 60) }

    val secValueMaxWidth = "00"
    val minutesValueMaxWidth = if (minutes < 10) {
        "0"
    } else {
        "00"
    }

    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(
        "$text $minutesValueMaxWidth:$secValueMaxWidth $units",
        DiiaTextStyle.t4TextSmallDescription
    ).size.width

    val textComponentWidth = remember { with(density) { widthInPixels.toDp() } }


    LaunchedEffect(Unit) {
        launch {
            while (minutes > 0 || seconds > 0) {
                delay(1000)
                if (seconds == 0) {
                    minutes--
                    seconds = 59
                } else {
                    seconds--
                }
            }
            onTimeOver()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = modifier
                .width(textComponentWidth),
            text = "$text ${minutes}:${String.format("%02d", seconds)} $units",
            textAlign = TextAlign.Start,
            style = DiiaTextStyle.t4TextSmallDescription,
            color = BlackAlpha40
        )
    }
}

@Preview
@Composable
fun TextCounterPreview() {
    TimerText(text = "Код діятиме ще ", "хв", 180, Modifier) {
    }
}
