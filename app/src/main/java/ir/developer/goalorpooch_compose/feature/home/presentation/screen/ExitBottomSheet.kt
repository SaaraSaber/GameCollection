package ir.developer.goalorpooch_compose.feature.home.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.DarkBackground
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.paddingRoundDialog
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicSmall
import ir.developer.goalorpooch_compose.core.theme.sizeRound
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AboutUsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onItemClick: () -> Unit
) {
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
                    text = stringResource(R.string.about_us),
                    color = Color.White,
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
                modifier = modifier.padding(top = paddingTop(), bottom = paddingTopMedium()),
                text = stringResource(R.string.description_about_us),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onItemClick() },
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
                        .padding(paddingRoundDialog())
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.sdp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.email),
                        contentDescription = "email",
                        modifier = modifier.size(sizePicSmall())
                    )
                    Column(modifier = modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.contact_support),
                            color = Color.White,
                            fontFamily = FontPeydaMedium,
                            fontSize = descriptionSize(),
                        )
                        Text(
                            modifier = modifier.padding(top = 4.sdp),
                            text = stringResource(R.string.description_contact_support),
                            color = Color.White,
                            fontFamily = FontPeydaMedium,
                            fontSize = 10.ssp,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

        }
    }
}

//@Composable
//fun ContactAppsDialog(
//    modifier: Modifier = Modifier,
//    onShowToast: () -> Unit,
//    onDismiss: () -> Unit,
//) {
//    var showToast by remember { mutableStateOf(false) }
//    val listApps = listOf(
//        AppModel(
//            id = 0,
//            name = stringResource(R.string.open_chest),
//            description = stringResource(R.string.description_open_chest),
//            image = R.drawable.pic_open_chest
//        ),
//        AppModel(
//            id = 1,
//            name = stringResource(R.string.pro_wallpaper),
//            description = stringResource(R.string.description_pro_wallpaper),
//            image = R.drawable.pic_pro_wallpaper
//        ),
//        AppModel(
//            id = 2,
//            name = stringResource(R.string.intelligence_test),
//            description = stringResource(R.string.description_intelligence_test),
//            image = R.drawable.pic_intelligence_test
//        ),
//        AppModel(
//            id = 3,
//            name = stringResource(R.string.check_list),
//            description = stringResource(R.string.description_check_list),
//            image = R.drawable.pic_check_list
//        ),
//        AppModel(
//            id = 4,
//            name = stringResource(R.string.dare_or_truth),
//            description = stringResource(R.string.description_dare_or_truth),
//            image = R.drawable.pic_dare_or_truth
//        )
//    )
//    val context = LocalContext.current
//    Box(modifier = Modifier.wrapContentHeight()) {
//        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//            Column(
//                modifier = modifier
//                    .padding(start = paddingRoundDialog(), end = paddingRoundDialog(), bottom = paddingRoundDialog())
//                    .fillMaxWidth()
//            ) {
//                Row(
//                    modifier = modifier
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = stringResource(R.string.apps),
//                        color = Color.White,
//                        fontFamily = FontPeydaBold,
//                        fontSize = titleSize()
//                    )
//                    Spacer(modifier = modifier.weight(1f))
//                    IconButton(
//                        onClick = { onDismiss() }
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.close_circle),
//                            contentDescription = "btn_close",
//                            modifier = modifier.size(20.sdp)
//                        )
//                    }
//
//                }
//                HorizontalDivider(modifier = modifier.padding(top = paddingTop()))
//
//                Text(
//                    modifier = modifier.padding(top = paddingTop(), bottom = paddingTop()),
//                    text = stringResource(R.string.description_apps),
//                    color = Color.White,
//                    fontFamily = FontPeydaMedium,
//                    fontSize = descriptionSize(),
//                    textAlign = TextAlign.Justify
//                )
//                listApps.forEach { item ->
//                    ItemApps(item = item, onClickItem = {
//                        try {
//                            val intent = Intent(Intent.ACTION_VIEW).apply {
//                                data = when (item.id) {
//                                    0 -> Uri.parse("https://cafebazaar.ir/app/ir.developre.chistangame")
//                                    1 -> Uri.parse("https://cafebazaar.ir/app/ir.forrtestt.wall1")
//                                    2 -> Uri.parse("https://cafebazaar.ir/app/com.example.challenginquestions")
//                                    3 -> Uri.parse("https://cafebazaar.ir/app/ir.developer.todolist")
//                                    4 -> Uri.parse("https://cafebazaar.ir/app/ir.codesphere.truthordare")
//                                    else -> null
//                                }
//                            }
//                            context.startActivity(intent)
//                        } catch (t: Throwable) {
//                            onShowToast()
//                        }
//                    })
//                }
//            }
//        }
//
//        if (showToast) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 16.sdp), // فاصله از بالای صفحه
//                contentAlignment = Alignment.TopCenter // قرارگیری در بالای صفحه
//            ) {
//                CustomToast(
//                    message = stringResource(R.string.message_catch),
//                    isVisible = true,
//                    color = R.color.yellow,
//                    icon = R.drawable.danger_circle,
//                    onDismiss = { showToast = false }
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitAppDialog(
    onDismiss: () -> Unit,
    onClickStar: () -> Unit,
    onClickExit: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkBackground,
        shape = RoundedCornerShape(topStart = 16.sdp, topEnd = 16.sdp),
    ) {
        UiExitAppDialog(
            onDismiss = onDismiss,
            onClickStar = onClickStar,
            onClickExit = onClickExit
        )
    }

}

@Composable
fun UiExitAppDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onClickStar: () -> Unit,
    onClickExit: () -> Unit,
) {
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
                    text = stringResource(R.string.exit),
                    color = Color.White,
                    fontSize = titleSize(),
                    fontFamily = FontPeydaBold
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
                modifier = modifier.padding(top = paddingTop(), bottom = paddingTopMedium()),
                text = stringResource(R.string.description_exit_app),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )
            Row(
                modifier = modifier
                    .padding(
                        top = paddingTop(),
                        start = paddingTopMedium(),
                        end = paddingTopMedium()
                    )
                    .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.sdp)
            ) {
                Card(
                    modifier = modifier
                        .weight(1f)
                        .clickable { onClickStar() }
                        .align(Alignment.CenterVertically),
                    colors = CardColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(sizeRound()),
                    border = BorderStroke(width = 1.sdp, color = Color.White)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(paddingTopMedium()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "star",
                            modifier = modifier.size(sizePicMedium())
                        )
                        Text(
                            modifier = modifier.padding(
                                top = paddingTopMedium()
                            ),
                            text = stringResource(R.string.rate),
                            color = Color.White,
                            fontFamily = FontPeydaMedium,
                            fontSize = descriptionSize(),
                            textAlign = TextAlign.Justify
                        )
                    }
                }

                Card(
                    modifier = modifier
                        .weight(1f)
                        .clickable { onClickExit() },
                    colors = CardColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(sizeRound()),
                    border = BorderStroke(width = 1.sdp, color = Color.White)
                ) {
                    Column(
                        modifier = modifier
                            .padding(paddingTopMedium())
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.exit),
                            contentDescription = "star",
                            modifier = modifier.size(sizePicMedium())
                        )
                        Text(
                            modifier = modifier.padding(
                                top = paddingTopMedium(),
                            ),
                            text = stringResource(R.string.exit),
                            color = Color.White,
                            fontFamily = FontPeydaMedium,
                            fontSize = descriptionSize(),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}