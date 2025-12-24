package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import ir.developer.goalorpooch_compose.core.theme.HihadaBrown
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.fontSizeButton
import ir.developer.goalorpooch_compose.core.theme.heightButton
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.sizePicLarge
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizeRoundBottomSheet
import ir.developer.goalorpooch_compose.core.theme.sizeRoundMax
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.FinishGameDialog
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.GameAppBar
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardSelectionEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardSelectionIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel.CardSelectionViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun CardSelectionScreen(
    navController: NavController,
    viewModel: CardSelectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CardSelectionEffect.NavigateToHome -> navController.popBackStack(
                    Routes.HOME,
                    inclusive = false
                )

                CardSelectionEffect.NavigateToDisplay -> navController.navigate(Routes.GOOLYAPOOCH_DISPLAY_CARDS)
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(CardSelectionIntent.OnBackClicked)
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
                GameAppBar(
                    title = stringResource(R.string.distribution_of_cards),
                    showBackButton = false,
                    onBackClick = {}
                )
                Row(
                    modifier = Modifier.padding(
                        top = paddingTop(),
                        start = paddingRound(),
                        end = paddingRound()
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(sizePicMedium()),
                        painter = painterResource(id = state.teamImageRes),
                        contentDescription = "pic_team_one"
                    )
                    Text(
                        modifier = Modifier.padding(start = paddingRound()),
                        text = stringResource(state.teamName),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = Color.White,
                        textAlign = TextAlign.Justify
                    )
                }
                Text(
                    modifier = Modifier.padding(
                        top = paddingTop(),
                        start = paddingRound(),
                        end = paddingRound()
                    ),
                    text = state.description,
                    fontSize = descriptionSize(),
                    fontFamily = FontPeydaMedium,
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )

                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(
                            start = paddingRound(),
                            end = paddingRound()
                        )
                        .fillMaxWidth()
                        .weight(1f)
                    ,
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(4.sdp),
                    horizontalArrangement = Arrangement.spacedBy(10.sdp),
                ) {
                    items(state.cards) { card ->
                        CardItem(
                            card = card,
                            onClick = {
                                viewModel.handleIntent(
                                    CardSelectionIntent.OnCardClicked(cardId = card.id)
                                )
                            })
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(paddingRound())
                        .fillMaxWidth()
                        .height(heightButton())
                        .align(Alignment.CenterHorizontally)
                        .alpha(if (state.currentSelectedCount != state.maxSelectionCount) 0.7f else 1f),
                    colors = ButtonColors(
                        containerColor = FenceGreen,
                        contentColor = Color.White,
                        disabledContainerColor = HihadaBrown,
                        disabledContentColor = HihadaBrown
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(sizeRoundMax()),
                    contentPadding = PaddingValues(0.dp),
                    enabled = state.currentSelectedCount == state.maxSelectionCount,
                    onClick = { viewModel.handleIntent(CardSelectionIntent.OnConfirmClicked) }) {
                    Text(
                        text = "تایید (${state.currentSelectedCount})",
                        color = Color.White,
                        fontSize = fontSizeButton(),
                        fontFamily = FontPeydaBold
                    )
                }

                if (state.showExitDialog) {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.handleIntent(CardSelectionIntent.OnExitDismissed) },
                        sheetState = sheetState,
                        shape = RoundedCornerShape(
                            topEnd = sizeRoundBottomSheet(),
                            topStart = sizeRoundBottomSheet()
                        ),
                        containerColor = FenceGreen
                    ) {
                        FinishGameDialog(
                            onClickExit = {
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion { viewModel.handleIntent(CardSelectionIntent.OnExitDismissed) }
                                viewModel.handleIntent(CardSelectionIntent.OnExitConfirmed)
                            },
                            onClickContinueGame = {
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion { viewModel.handleIntent(CardSelectionIntent.OnExitDismissed) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardItem(card: GameCardModel, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isSelected) 180f else 0f,
        animationSpec = tween(durationMillis = 500), label = ""
    )
    val alpha by animateFloatAsState(
        targetValue = if (card.isSelected) 0.5f else 1f,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(8.sdp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .background(Color.Transparent)
            .graphicsLayer {
                rotationY = rotation
                this.alpha = alpha
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pic_card),
            contentDescription = null,
            modifier = Modifier
                .width(sizePicLarge())
//                .alpha(if (isSelected) 0.4f else 1f)
            ,
            contentScale = ContentScale.Crop
        )
    }
}