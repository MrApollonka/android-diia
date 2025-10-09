package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnNumAtm
import ua.gov.diia.ui_base.components.atom.button.BtnNumAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconBiometricAtm
import ua.gov.diia.ui_base.components.atom.icon.IconRemoveNumAtm
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction

@Composable
fun NumButtonTileMlcLandscape(
    modifier: Modifier = Modifier,
    hasBiometric: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isSmallScreen = configuration.screenHeightDp.dp < 320.dp

    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 4.dp else 16.dp)
    ) {
        Row(
            Modifier
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "1", number = 1),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 32.dp)
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "2", number = 2),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "3", number = 3),
                onUIAction = onUIAction
            )
        }

        Row(
            Modifier
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "4", number = 4),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 32.dp)
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "5", number = 5),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "6", number = 6),
                onUIAction = onUIAction
            )
        }

        Row(
            Modifier
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "7", number = 7),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 32.dp)
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "8", number = 8),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "9", number = 9),
                onUIAction = onUIAction
            )
        }

        Row(
            Modifier
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (hasBiometric) {
                IconBiometricAtm(
                    modifier = Modifier
                        .size(60.dp)
                        .semantics {
                            role = Role.Button
                        },
                    onUIAction = onUIAction
                )
            } else {
                // For proper line arrangement
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                )
            }

            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 32.dp)
                    .semantics {
                        role = Role.Button
                    },
                data = BtnNumAtmData(id = "0", number = 0),
                onUIAction = onUIAction
            )

            IconRemoveNumAtm(
                modifier = Modifier
                    .size(60.dp)
                    .semantics {
                        role = Role.Button
                    },
                onUIAction = onUIAction
            )
        }
    }
}


@Composable
@Preview
fun NumButtonTileMoleculeLandscapePreview() {
    NumButtonTileMlcLandscape(
        hasBiometric = false
    ) {
        /* no-op */
    }
}

@Composable
@Preview
fun NumButtonTileMoleculeLandscapeWithBiometricPreview() {
    NumButtonTileMlcLandscape(
        hasBiometric = true
    ) {
        /* no-op */
    }
}