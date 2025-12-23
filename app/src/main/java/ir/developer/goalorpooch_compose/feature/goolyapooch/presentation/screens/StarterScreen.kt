package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.navigation.Routes
import ir.developer.goalorpooch_compose.core.theme.FenceGreen
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.heightButton
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.paddingTopLarge
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicLarge
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizeRound
import ir.developer.goalorpooch_compose.core.theme.sizeRoundBottomSheet
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.StarterOptionModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.GameAppBar
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel.StarterViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StarterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is StarterEffect.NavigateBack -> navController.popBackStack()
                is StarterEffect.NavigateToCardSelection -> {
                    navController.navigate(Routes.getCardSelectionRoute(effect.teamId))
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Box(modifier = Modifier.fillMaxSize()) {
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
                        title = stringResource(R.string.determining_the_initiator),
                        showBackButton = true,
                        onBackClick = { viewModel.handelIntent(StarterIntent.OnBackClicked) }
                    )

                    Text(
                        modifier = Modifier.padding(
                            end = paddingRound(),
                            start = paddingRound(),
                            top = paddingTop(),
                            bottom = 30.sdp
                        ),
                        text = stringResource(R.string.starter_description),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = Color.White,
                        textAlign = TextAlign.Justify
                    )
                    LazyColumn(
                        modifier = modifier.padding(horizontal = paddingRound()),
                        verticalArrangement = Arrangement.spacedBy(paddingTop())
                    ) {
                        items(state.options) { item ->
                            ItemStarter(item = item, onClick = {
                                viewModel.handelIntent(StarterIntent.OnOptionClicked(item.type))
                            })
                        }
                    }
                }
            }
            if (state.showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.handelIntent(StarterIntent.OnBottomSheetDismiss) },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(sizeRoundBottomSheet()),
                    containerColor = FenceGreen
                ) {
                    StarterBottomSheetContent(selectedTeam = state.selectedTeam, onDismiss = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible)
                                viewModel.handelIntent(StarterIntent.OnBottomSheetDismiss)
                        }
                    }, onConfirm = {
                        viewModel.handelIntent(
                            StarterIntent.OnConfirmStarter
                        )
                    })
                }
            }
        }
    }
}

@Composable
fun ItemStarter(item: StarterOptionModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .border(1.sdp, Color.White, RoundedCornerShape(sizeRound()))
            .clip(RoundedCornerShape(sizeRound()))
            .fillMaxWidth()
            .background(FenceGreen)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(paddingTopMedium())
                .size(sizePicMedium()),
            painter = painterResource(id = item.icon),
            contentDescription = null
        )

        Text(
            text = stringResource(item.title),
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun StarterBottomSheetContent(
    selectedTeam: StarterTeam,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val selectedImage =
        if (selectedTeam == StarterTeam.TEAM_1) R.drawable.pic_team_one else R.drawable.pic_team_two
    val selectedText =
        if (selectedTeam == StarterTeam.TEAM_1) R.string.start_team_one else R.string.start_team_two
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(paddingRound())
                .size(sizePicLarge()),
            painter = painterResource(id = selectedImage),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(paddingTopMedium()),
            text = stringResource(selectedText),
            fontSize = titleSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White,
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
                onClick = { onConfirm() }
            ) {
                Text(
                    text = stringResource(R.string.choose_card),
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
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.cansel),
                    fontSize = descriptionSize(),
                    fontFamily = FontPeydaMedium,
                    color = Color.White
                )
            }
        }
    }
}