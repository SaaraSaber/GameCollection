package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
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
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicSmall
import ir.developer.goalorpooch_compose.core.theme.sizeRoundBottomSheet
import ir.developer.goalorpooch_compose.core.theme.sizeRoundMax
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.FinishGameDialog
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.GameAppBar
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardDisplayEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardDisplayIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel.CardDisplayViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayCardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CardDisplayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    BackHandler {
        viewModel.handleIntent(CardDisplayIntent.OnBackClicked)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CardDisplayEffect.NavigateToNextTeamSelection -> {
                    navController.navigate(
                        Routes.getCardSelectionRoute(effect.nextTeamId, effect.starterTeamId)
                    ) {
                        popUpTo(Routes.GOOLYAPOOCH_CARD_SELECTION) { inclusive = true }
                    }
                }

                is CardDisplayEffect.NavigateToMainGame -> {
                     navController.navigate(Routes.GOOLYAPOOCH_START_GAME)
                }

                CardDisplayEffect.NavigateToHome -> navController.popBackStack(Routes.HOME, inclusive = false)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .paint(
                        painterResource(id = R.drawable.main_background),
                        contentScale = ContentScale.Crop
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameAppBar(
                    title = stringResource(R.string.show_cards),
                    showBackButton = false,
                    onBackClick = {}
                )
                Image(
                    modifier = modifier
                        .padding(paddingTop())
                        .size(sizePicMedium()),
                    painter = painterResource( state.teamImageRes),
                    contentDescription = "pic"
                )
                Text(
                    text = stringResource(state.teamNameRes),
                    color = Color.White,
                    fontSize = descriptionSize(),
                    fontFamily = FontPeydaMedium
                )

                LazyColumn(
                    modifier = modifier
                        .padding(
                            top = paddingTopMedium(),
                            start = paddingRound(),
                            end = paddingRound()
                        )
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.sdp),
                ) {
                    itemsIndexed(state.displayedCards) { index ,card->
                        ItemSelectedCard(
                            card = card,
                            isLastItem = index == state.displayedCards.lastIndex
                        )
                    }
                }
                Button(
                    modifier = modifier
                        .padding(paddingRound())
                        .fillMaxWidth()
                        .height(heightButton())
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonColors(
                        containerColor = FenceGreen,
                        contentColor = Color.White,
                        disabledContainerColor = HihadaBrown,
                        disabledContentColor = HihadaBrown
                    ),
                    border = BorderStroke(1.sdp, Color.White),
                    shape = RoundedCornerShape(sizeRoundMax()),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {viewModel.handleIntent(CardDisplayIntent.OnNextStageClicked)}
                ) {
                    Text(
                        text = stringResource(state.nextButtonTextRes),
                        fontSize = fontSizeButton(),
                        fontFamily = FontPeydaBold
                    )
                }

                if (state.showExitDialog) {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.handleIntent(CardDisplayIntent.OnExitDismissed) },
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
                                    .invokeOnCompletion { viewModel.handleIntent(CardDisplayIntent.OnExitDismissed) }
                                viewModel.handleIntent(CardDisplayIntent.OnExitConfirmed)
                            },
                            onClickContinueGame = {
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion { viewModel.handleIntent(CardDisplayIntent.OnExitDismissed) }
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ItemSelectedCard(card: GameCardModel, isLastItem: Boolean, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.sdp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.sdp)
            ) {
                Image(
                    modifier = Modifier
                        .width(sizePicSmall()),
                    painter = painterResource(id = R.drawable.pic_card),
                    contentDescription = "image"
                )
                Text(
                    text = stringResource(card.title),
                    color = Color.White,
                    fontSize = descriptionSize(),
                    fontFamily = FontPeydaBold
                )
            }
            Text(
                text = stringResource(card.description),
                color = Color.White,
                fontSize = descriptionSize(),
                fontFamily = FontPeydaMedium,
                textAlign = TextAlign.Justify
            )
            if (!isLastItem) {
                HorizontalDivider(
                    modifier = modifier
                        .padding(
                            top = paddingTopMedium(),
                            bottom = paddingTop(),
                            end = 50.sdp,
                            start = 50.sdp
                        ),
                    thickness = DividerDefaults.Thickness
                )
            }
        }
    }
}