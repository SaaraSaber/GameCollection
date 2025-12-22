package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.navigation.Routes
import ir.developer.goalorpooch_compose.core.theme.FenceGreen
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.GrayDisable
import ir.developer.goalorpooch_compose.core.theme.HihadaBrown
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.fontSizeButton
import ir.developer.goalorpooch_compose.core.theme.heightButton
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.paddingTopLarge
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicSmall
import ir.developer.goalorpooch_compose.core.theme.sizeRoundMax
import ir.developer.goalorpooch_compose.core.theme.textSize
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingUiItem
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel.SettingViewModel
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingEffect.NavigateBack -> navController.popBackStack()
                is SettingEffect.NavigateNextLevel -> {
                    navController.navigate(Routes.GOOLYAPOOCH_DETERMINING_THE_GAME_STARTER)
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .paint(
                        painterResource(id = R.drawable.main_background),
                        contentScale = ContentScale.Crop
                    )
            ) {

//                AppBar(
//                    title = stringResource(R.string.setting),
//                    showBackButton = true,
//                    navController = navController
//                )

                Text(
                    modifier = Modifier.padding(
                        top = paddingTop(),
                        start = paddingRound(),
                        end = paddingRound()
                    ),
                    text = stringResource(R.string.description_setting),
                    fontSize = descriptionSize(),
                    fontFamily = FontPeydaMedium,
                    color = White,
                    textAlign = TextAlign.Justify
                )

                LazyColumn(
                    modifier = modifier
                        .padding(top = paddingTopLarge())
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(paddingTopMedium()),
                    verticalArrangement = Arrangement.spacedBy(paddingRound())
                ) {
                    items(state.uiItems) { item ->
                        SettingRow(
                            item = item,
                            onPlusClick = {
                                viewModel.handelIntent(
                                    SettingIntent.OnChangeValue(
                                        type = item.type,
                                        isIncrease = true
                                    )
                                )
                            },
                            onMinusClick = {
                                viewModel.handelIntent(
                                    SettingIntent.OnChangeValue(
                                        type = item.type,
                                        isIncrease = false
                                    )
                                )
                            })
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .padding(paddingRound())
                        .fillMaxWidth()
                        .height(heightButton())
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonColors(
                        containerColor = FenceGreen,
                        contentColor = White,
                        disabledContainerColor = HihadaBrown,
                        disabledContentColor = HihadaBrown
                    ),
                    border = BorderStroke(1.dp, White),
                    shape = RoundedCornerShape(sizeRoundMax()),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { viewModel.handelIntent(SettingIntent.OnNextLevelClicked) }) {
                    Text(
                        text = stringResource(R.string.next_level),
                        fontSize = fontSizeButton(),
                        fontFamily = FontPeydaBold
                    )
                }
            }

        }
    }
}

@Composable
fun SettingRow(
    item: SettingUiItem,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(item.title),
            textAlign = TextAlign.Start,
            fontSize = textSize(),
            fontFamily = FontPeydaMedium,
            color = White
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                modifier = Modifier
                    .size(sizePicSmall()),
                colors = ButtonColors(
                    containerColor = FenceGreen,
                    contentColor = White,
                    disabledContainerColor = GrayDisable,
                    disabledContentColor = GrayDisable
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                enabled = item.value < item.maxValue,
                onClick = { onPlusClick() }
            ) {
                Icon(
                    modifier = Modifier.size(12.sdp),
                    painter = painterResource(id = R.drawable.increase),
                    contentDescription = null,
                    tint = White
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 8.sdp, end = 8.sdp)
                    .align(Alignment.Bottom)
                    .width(40.sdp),
                text = item.value.toString(),
                textAlign = TextAlign.Center,
                fontSize = textSize(),
                fontFamily = FontPeydaMedium,
                color = White
            )
            ElevatedButton(
                modifier = Modifier
                    .size(sizePicSmall()),
                colors = ButtonColors(
                    containerColor = FenceGreen,
                    contentColor = White,
                    disabledContainerColor = GrayDisable,
                    disabledContentColor = GrayDisable
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                enabled = item.value > item.minValue,
                onClick = { onMinusClick() }
            ) {
                Icon(
                    modifier = Modifier.size(12.sdp),
                    painter = painterResource(id = R.drawable.decrease),
                    contentDescription = null,
                    tint = White
                )
            }
        }
    }
}