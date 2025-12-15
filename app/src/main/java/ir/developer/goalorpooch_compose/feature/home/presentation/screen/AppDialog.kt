package ir.developer.goalorpooch_compose.feature.home.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.DarkBackground
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.White
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingRoundDialog
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.sizeIcon
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizeRound
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAppDialog(
    items: List<AppItemModel>,
    onDismiss: () -> Unit,
    onClickItem: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkBackground,
        shape = RoundedCornerShape(topStart = 16.sdp, topEnd = 16.sdp),
    ) {
        UiContactAppsDialog(
            items = items,
            onDismiss = onDismiss,
            onClickItem = onClickItem
        )
    }
}

@Composable
fun UiContactAppsDialog(
    modifier: Modifier = Modifier,
    items: List<AppItemModel>,
    onDismiss: () -> Unit,
    onClickItem: (String) -> Unit
) {

    Box(modifier = Modifier.wrapContentHeight()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = modifier
                    .padding(
                        start = paddingRoundDialog(),
                        end = paddingRoundDialog(),
                        bottom = paddingRoundDialog()
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.apps),
                        color = White,
                        fontFamily = FontPeydaBold,
                        fontSize = titleSize()
                    )
                    Spacer(modifier = modifier.weight(1f))
                    IconButton(
                        onClick = { onDismiss() }
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
                    modifier = modifier.padding(top = paddingTop(), bottom = paddingTop()),
                    text = stringResource(R.string.description_apps),
                    color = Color.White,
                    fontFamily = FontPeydaMedium,
                    fontSize = descriptionSize(),
                    textAlign = TextAlign.Justify
                )

                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    items(items) { app ->
                        AppRowItem(
                            app = app,
                            onClick = { onClickItem(app.packageName) }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun AppRowItem(modifier: Modifier = Modifier, app: AppItemModel, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        shape = RoundedCornerShape(sizeRound()),
        border = BorderStroke(width = 1.sdp, color = Color.White)
    ) {
        Row(
            modifier = modifier
                .padding(paddingRound())
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.sdp)
        ) {
            Image(
                painter = painterResource(app.iconRes),
                contentDescription = null,
                modifier = modifier.size(sizePicMedium()),
                contentScale = ContentScale.Crop
            )
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = stringResource(app.name),
                    color = Color.White,
                    fontFamily = FontPeydaMedium,
                    fontSize = descriptionSize(),
                )
                Text(
                    modifier = modifier.padding(top = 4.sdp),
                    text = stringResource(app.description),
                    color = Color.White,
                    fontFamily = FontPeydaMedium,
                    fontSize = 10.ssp,
                    textAlign = TextAlign.Justify
                )
            }
            Image(
                painter = painterResource(R.drawable.file_download),
                contentDescription = null,
                modifier = modifier.size(sizeIcon()),
            )
        }
    }
}