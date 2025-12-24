package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.FenceGreen
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.heightButton
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.paddingTopLarge
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.kaaveh.sdpcompose.sdp


@Composable
fun FinishGameDialog(
    modifier: Modifier = Modifier,
    onClickExit: () -> Unit,
    onClickContinueGame: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.exit),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
                Spacer(modifier = modifier.weight(1f))
                IconButton(
                    onClick = onClickContinueGame
                ) {
                    Image(
                        painter = painterResource(R.drawable.close_circle),
                        contentDescription = "btn_close",
                        modifier = modifier.size(20.sdp)
                    )
                }

            }
            HorizontalDivider(modifier = modifier.padding(top = paddingTop()))

            Text(
                modifier = modifier.padding(top = paddingTop(), bottom = paddingTopMedium()),
                text = stringResource(R.string.description_exit_game),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )

            Row(modifier = Modifier.padding(top = paddingTopLarge(), bottom = paddingRound())) {
                Button(
                    modifier = Modifier
                        .padding(start = paddingRound(), end = 5.sdp)
                        .height(heightButton())
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = FenceGreen
                    ),
                    onClick = { onClickContinueGame() }
                ) {
                    Text(
                        text = stringResource(R.string.continue_game),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = FenceGreen
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(end = paddingRound(), start = 5.sdp)
                        .height(heightButton())
                        .weight(1f),
                    border = BorderStroke(1.sdp, Color.White),
                    onClick = onClickExit
                ) {
                    Text(
                        text = stringResource(R.string.exit),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
