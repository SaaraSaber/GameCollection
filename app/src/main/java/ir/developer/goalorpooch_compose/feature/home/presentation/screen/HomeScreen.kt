package ir.developer.goalorpooch_compose.feature.home.presentation.screen

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.ChampionBlue
import ir.developer.goalorpooch_compose.core.theme.DarkBackground
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.IconSize
import ir.developer.goalorpooch_compose.core.theme.Leila
import ir.developer.goalorpooch_compose.core.theme.MiamiBlue
import ir.developer.goalorpooch_compose.core.theme.NumberFontSize
import ir.developer.goalorpooch_compose.core.theme.PaddingScreenSize
import ir.developer.goalorpooch_compose.core.theme.Rose
import ir.developer.goalorpooch_compose.core.theme.RoundedCornerSize
import ir.developer.goalorpooch_compose.core.theme.White
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeDialogType
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeEffect
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeIntent
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeState
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.openAppInMarket
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.openEmail
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.openMarket
import ir.developer.goalorpooch_compose.feature.home.presentation.viewmodel.HomeViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        state = state,
        viewModel = viewModel,
        onIntent = viewModel::homeIntentHandel,
        navController = navController
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    viewModel: HomeViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.OpenMarketForRate -> {
                    context.openMarket()
                }

                is HomeEffect.Navigation -> {
                    navController.navigate(effect.route)
                }

                is HomeEffect.FinishApp -> {
                    activity?.finishAffinity()
                }

                is HomeEffect.OpenEmail -> {
                    context.openEmail()
                }

                is HomeEffect.OpenExternalApp -> {
                    context.openAppInMarket(effect.packageName)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground, topBar = {
            HomeTopBar(
                modifier = Modifier.statusBarsPadding(),
                coinBalance = state.coinBalance,
                onExitClick = { onIntent(HomeIntent.ChangeDialogState(HomeDialogType.EXIT)) },
                onInfoClick = { onIntent(HomeIntent.ChangeDialogState(HomeDialogType.INFO)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = DarkBackground)
        ) {
            Column {
                HorizontalDivider(
                    modifier = modifier.padding(
                        start = PaddingScreenSize(),
                        end = PaddingScreenSize(),
                        bottom = PaddingScreenSize()
                    ), color = Leila
                )
                LazyColumn(
                    contentPadding = PaddingValues(8.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    items(state.gameItems) { game ->
                        GameList(
                            background = game.background,
                            nameGame = game.name,
                            iconGame = game.icon,
                            onClick = { onIntent(HomeIntent.OnGameClicked(game.route)) }
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp),
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(state.otherItems) { other ->
                        OtherItem(name = other.name, icon = other.icon, onCliCk = {
                            onIntent(
                                HomeIntent.ChangeDialogState(other.targetDialog)
                            )
                        })
                    }
                }
            }
        }

        when (state.activeDialog) {
            HomeDialogType.EXIT -> {
                ExitAppDialog(
                    onDismiss = { onIntent(HomeIntent.OnDialogDismissed) },
                    onClickStar = { onIntent(HomeIntent.OnRateClicked) },
                    onClickExit = { onIntent(HomeIntent.OnExitConfirmed) }
                )
            }

            HomeDialogType.APPS -> {
                ContentAppDialog(
                    items = state.appItems,
                    onDismiss = { onIntent(HomeIntent.OnDialogDismissed) },
                    onClickItem = { pkg ->
                        onIntent(
                            HomeIntent.OnAppItemClicked(packageName = pkg)
                        )
                    })
            }

            HomeDialogType.INFO -> {
                InfoDialog(
                    onDismiss = { onIntent(HomeIntent.OnDialogDismissed) },
                    onItemClick = { onIntent(HomeIntent.OnEmailClicked) }
                )
            }

            HomeDialogType.SHOP -> {}
            HomeDialogType.NONE -> {}
        }

    }

}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    coinBalance: Int,
    onExitClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingScreenSize()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(24.sdp)
                    .background(
                        color = ChampionBlue,
                        shape = RoundedCornerShape(RoundedCornerSize())
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onExitClick() }, modifier = modifier.size(IconSize())) {
                    Icon(
                        painter = painterResource(R.drawable.exit_icon),
                        contentDescription = null,
                        tint = Rose
                    )
                }
            }

            Box(
                modifier = modifier
                    .size(24.sdp)
                    .background(
                        color = ChampionBlue,
                        shape = RoundedCornerShape(RoundedCornerSize())
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onInfoClick() }, modifier = modifier.size(IconSize())) {
                    Icon(
                        painter = painterResource(R.drawable.info_circle),
                        contentDescription = null,
                        tint = MiamiBlue
                    )
                }
            }
        }

        Box(
            modifier = modifier
                .background(
                    color = ChampionBlue,
                    shape = RoundedCornerShape(RoundedCornerSize())
                )
                .padding(top = 4.sdp, bottom = 4.sdp, start = 6.sdp, end = 6.sdp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = coinBalance.toString(), color = White, fontSize = NumberFontSize())
                Image(
                    painter = painterResource(R.drawable.coin_icon),
                    contentDescription = null,
                    modifier = modifier.size(IconSize())
                )
            }
        }
    }
}

@Composable
fun GameList(
    modifier: Modifier = Modifier,
    background: Int,
    nameGame: Int,
    iconGame: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.sdp)
            .clip(shape = RoundedCornerShape(10.sdp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(background),
            contentDescription = null,
            modifier = modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 10.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Image(
                painter = painterResource(iconGame),
                contentDescription = null,
                modifier = modifier.fillMaxHeight(),
                alignment = Alignment.BottomCenter,
                contentScale = ContentScale.Fit
            )
            Text(
                text = stringResource(nameGame),
                color = White,
                fontSize = 20.ssp,
                fontFamily = FontPeydaBold
            )
        }
    }
}

@Composable
fun OtherItem(modifier: Modifier = Modifier, name: Int, icon: Int, onCliCk: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(8.sdp))
            .background(color = ChampionBlue)
            .clickable { onCliCk() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.padding(vertical = 8.sdp),
            verticalArrangement = Arrangement.spacedBy(8.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = modifier.size(50.sdp)
            )
            Text(
                text = stringResource(name),
                fontSize = 20.ssp,
                color = White,
                fontFamily = FontPeydaBold
            )
        }
    }
}