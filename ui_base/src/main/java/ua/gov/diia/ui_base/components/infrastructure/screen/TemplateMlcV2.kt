package ua.gov.diia.ui_base.components.infrastructure.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryLargeAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryLargeAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeLargeAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeLargeAtmData
import ua.gov.diia.ui_base.components.atom.icon.MediumIconAtm
import ua.gov.diia.ui_base.components.atom.icon.MediumIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun TemplateMlcV2(
    modifier: Modifier = Modifier,
    data: TemplateMlcV2Data,
    onUIAction: (UIAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortraitOrientation = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            data.mediumIconAtm?.let { lMediumIconAtm ->
                MediumIconAtm(
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    data = lMediumIconAtm,
                    onUIAction = onUIAction
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        heading()
                    },
                text = data.title.asString(),
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.h2MediumHeading
            )
            val description = data.description?.asString()
            if (!description.isNullOrBlank()){
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    text = description,
                    textAlign = TextAlign.Center,
                    style = DiiaTextStyle.t3TextBody
                )
            }
            if (isPortraitOrientation) {
                createBtnPrimaryLargeAtm(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    btnPrimaryLargeAtmData = data.btnPrimaryLargeAtmData,
                    onUIAction = onUIAction
                )
                createBtnStrokeLargeAtmData(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    bnStrokeLargeAtmData = data.btnStrokeLargeAtmData,
                    onUIAction = onUIAction
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    createBtnPrimaryLargeAtm(
                        modifier = Modifier
                            .weight(1F),
                        btnPrimaryLargeAtmData = data.btnPrimaryLargeAtmData,
                        onUIAction = onUIAction
                    )
                    createBtnStrokeLargeAtmData(
                        modifier = Modifier
                            .weight(1F),
                        bnStrokeLargeAtmData = data.btnStrokeLargeAtmData,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class TemplateMlcV2Data(
    val mediumIconAtm: MediumIconAtmData?,
    val title: UiText,
    val description: UiText? = null,
    val btnPrimaryLargeAtmData: BtnPrimaryLargeAtmData? = null,
    val btnStrokeLargeAtmData: BtnStrokeLargeAtmData? = null
) : UIElementData

@SuppressLint("ComposableNaming")
@Composable
private fun createBtnPrimaryLargeAtm(
    modifier: Modifier,
    btnPrimaryLargeAtmData: BtnPrimaryLargeAtmData?,
    onUIAction: (UIAction) -> Unit
) {
    btnPrimaryLargeAtmData?.let { lBtnPrimaryLargeAtmData ->
        BtnPrimaryLargeAtm(
            modifier = modifier
                .semantics {
                    role = Role.Button
                },
            data = lBtnPrimaryLargeAtmData,
            onUIAction = onUIAction
        )
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun createBtnStrokeLargeAtmData(
    modifier: Modifier,
    bnStrokeLargeAtmData: BtnStrokeLargeAtmData?,
    onUIAction: (UIAction) -> Unit
) {
    bnStrokeLargeAtmData?.let { lBtnStrokeLargeAtmData ->
        BtnStrokeLargeAtm(
            modifier = modifier
                .semantics {
                    role = Role.Button
                },
            data = lBtnStrokeLargeAtmData,
            onUIAction = onUIAction
        )
    }
}