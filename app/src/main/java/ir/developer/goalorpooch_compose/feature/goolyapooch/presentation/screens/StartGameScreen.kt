package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.navigation.Routes
import ir.developer.goalorpooch_compose.core.theme.FenceGreen
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.FontPeydaMedium
import ir.developer.goalorpooch_compose.core.theme.descriptionSize
import ir.developer.goalorpooch_compose.core.theme.mediumAlpha
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTopLarge
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium_two
import ir.developer.goalorpooch_compose.core.theme.sizePicSmall
import ir.developer.goalorpooch_compose.core.theme.sizePicVerySmall
import ir.developer.goalorpooch_compose.core.theme.sizeRound
import ir.developer.goalorpooch_compose.core.theme.sizeRoundBottomSheet
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetCards
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetConfirmCube
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetContactResultDuel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetCube
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetOpeningDuel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetResultOfThisRound
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetResultShahGoal
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.BottomSheetWinner
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.CustomToast
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components.FinishGameDialog
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.ToastType
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel.StartGameViewModel
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StartGameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StartGameViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden })
    val scope = rememberCoroutineScope()

    BackHandler {
        viewModel.handleIntent(StartGameIntent.OnBackClicked)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                StartGameEffect.NavigateToHome -> {
                    navController.navigate(Routes.HOME) { popUpTo(0) }
                }

                StartGameEffect.NavigateToSetting -> {
                    navController.navigate(Routes.GOOLYAPOOCH_SETTING) { popUpTo(0) }
                }
            }
        }
    }

    Scaffold { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(R.drawable.main_background),
                        contentScale = ContentScale.Crop
                    )
            ) {
                Column(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
//...............head
                    HeaderGame(
                        duel = false,
                        scoreTeamOne = state.team1.score,
                        scoreTeamTwo = state.team2.score,
                        whichTeamHasGoal =
                            when {
                                state.team1.hasGoal -> 0
                                state.team2.hasGoal -> 1
                                else -> 2
                            }
                    )

//...............table
                    if (state.isDuelMode) {
                        TableDuel(
                            whichTeamHasGoal =
                                when {
                                    state.team1.hasGoal -> 0
                                    state.team2.hasGoal -> 1
                                    else -> 2
                                }
//                                if (state.team1.hasGoal) 0 else if (state.team1.hasGoal) 1 else 2
                        )
                    } else {
                        TableGame(
                            whichTeamHasGoal =
                                when {
                                    state.team1.hasGoal -> 0
                                    state.team2.hasGoal -> 1
                                    else -> 2
                                },
                            remainingTime = state.timerValue,
                            showTimer = state.isTimerRunning,
                            isVisibilityShahGoal = state.isShahGoalMode,

                            )
                    }

// ............... Time Button ...............
                    if (!state.isDuelMode) {
                        Button(
                            modifier = modifier
                                .wrapContentWidth()
                                .height(50.sdp),
                            onClick = { viewModel.handleIntent(StartGameIntent.OnTimerToggleClicked) },
                            colors = ButtonDefaults.buttonColors(containerColor = FenceGreen),
                            shape = RoundedCornerShape(sizeRound()),
                            border = BorderStroke(width = 1.sdp, color = Color.White)
                        ) {
                            Text(
                                modifier = modifier.padding(end = paddingRound()),
                                text = stringResource(state.timerButtonTextRes),
                                fontSize = descriptionSize(),
                                fontFamily = FontPeydaMedium,
                                color = Color.White
                            )

                            Icon(
                                painter = painterResource(state.timerButtonIconRes),
                                contentDescription = "time",
                                modifier = modifier.size(
                                    sizePicVerySmall()
                                )
                            )
                        }
                    }

//...................duelButton
                    if (state.isDuelMode) {
                        Button(
                            modifier = modifier
                                .padding(bottom = paddingTopLarge())
                                .wrapContentWidth()
                                .height(50.sdp),
                            onClick = {
                                TODO("DuelCard")
//                                viewModel.handleIntent(StartGameIntent.)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FenceGreen),
                            shape = RoundedCornerShape(sizeRound()),
                            border = BorderStroke(width = 1.sdp, color = Color.White)
                        ) {
                            Text(
                                modifier = modifier.padding(end = paddingRound()),
                                text = stringResource(R.string.result_duel),
                                fontSize = descriptionSize(),
                                fontFamily = FontPeydaMedium,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(R.drawable.note),
                                contentDescription = "note",
                                modifier = modifier.size(
                                    sizePicVerySmall()
                                )
                            )
                        }
                    }

//.................box
                    if (!state.isDuelMode) {
                        val hasGoalTeamId =
                            when {
                                state.team1.hasGoal -> 0
                                state.team2.hasGoal -> 1
                                else -> -1
                            }
                        if (hasGoalTeamId != -1) {
                            TeamInfoSection(
                                infoTeam = if (hasGoalTeamId == 0) state.team1 else state.team2,
                                oppositeTeamCardsCount = if (hasGoalTeamId == 0) state.team2.cards.size else state.team1.cards.size,
                                enableShahGoal = state.isShahGoalMode,
                                emptyHandCount = state.emptyHandCount,
                                onIntent = { intent -> viewModel.handleIntent(intent) }
                            )
                        }
                    }
                }

                // ............... Bottom Sheets (Dialog Management) ...............

                if (state.activeDialog !is GameDialogState.None) {
                    ModalBottomSheet(
                        onDismissRequest = {},
                        sheetState = sheetState,
                        shape = RoundedCornerShape(
                            topEnd = sizeRoundBottomSheet(),
                            topStart = sizeRoundBottomSheet()
                        ),
                        containerColor = FenceGreen
                    ) {
                        when (val dialog = state.activeDialog) {
                            is GameDialogState.Cards -> {
                                val hasGoalTeamId = if (state.team1.hasGoal) 0 else 1

                                // ۲. انتخاب تیم "هدف" (تیمی که کارت‌هاش نمایش داده میشه)
                                val targetTeam =
                                    if (hasGoalTeamId == 0) state.team2 else state.team1

                                // ۳. فیلتر کردن کارت‌های غیرفعال (اگر منطق disable دارید)
                                // نکته: در مدل جدید ما کارت‌ها رو کلا از لیست حذف میکنیم، پس همون لیست cards کافیه
                                val cardsToShow = targetTeam.cards

                                BottomSheetCards(
                                    availableCards = cardsToShow,
                                    isTargetTeamOne = (targetTeam.id == 0), // آیا کارت‌های تیم ۱ رو نشون میدیم؟
                                    onDismiss = {
                                        viewModel.handleIntent(StartGameIntent.OnDismissDialog)
                                    },
                                    onConfirm = { cardId ->
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnCardSelected(
                                                    cardId
                                                )
                                            )
                                            viewModel.handleIntent(
                                                StartGameIntent.OnCardSelectedInDialog(
                                                    cardId
                                                )
                                            )
                                            viewModel.handleIntent(StartGameIntent.OnConfirmCardUsage)
                                        }
                                    }
                                )
                            }

                            is GameDialogState.ConfirmCube -> {
                                val score =
                                    (state.activeDialog as GameDialogState.ConfirmCube).number

                                // ۲. تشخیص تیم درخواست‌کننده (تیمی که گل دارد)
                                val isTeamOneHolder = state.team1.hasGoal

                                BottomSheetConfirmCube(
                                    isRequestingTeamOne = isTeamOneHolder,
                                    score = score,
                                    onConfirm = {
                                        // بستن شیت و اعمال تغییرات در ویومدل
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(StartGameIntent.OnCubeConfirmed)
                                        }
                                    },
                                    onDismiss = {
                                        // کنسل کردن (بستن دیالوگ بدون تغییر)
                                        viewModel.handleIntent(StartGameIntent.OnDismissDialog)
                                    }
                                )
                            }

                            GameDialogState.Cube -> {
                                val isTeamOneHavingGoal = state.team1.hasGoal

                                BottomSheetCube(
                                    isTeamOne = isTeamOneHavingGoal,
                                    onDismiss = {
                                        viewModel.handleIntent(StartGameIntent.OnDismissDialog)
                                    },
                                    onConfirm = { selectedScore ->
                                        // بستن شیت فعلی و رفتن به مرحله بعد (تاییدیه)
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnCubeNumberSelected(
                                                    selectedScore
                                                )
                                            )
                                        }
                                    }
                                )
                            }

                            GameDialogState.DuelResult -> {
                                val isTeamOneHolder = state.team1.hasGoal

                                BottomSheetContactResultDuel(
                                    isTeamOneHolder = isTeamOneHolder,
                                    onResult = { result ->
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnDuelResult(
                                                    result
                                                )
                                            )
                                        }
                                    }
                                )
                            }

                            GameDialogState.ExitGame -> {
                                FinishGameDialog(
                                    onClickExit = {
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnDismissDialog
                                            )
                                        }
                                        viewModel.handleIntent(StartGameIntent.OnExitConfirmed)
                                    },
                                    onClickContinueGame = {
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnDismissDialog
                                            )
                                        }
                                    }
                                )
                            }

                            GameDialogState.OpeningDuel -> {
                                BottomSheetOpeningDuel(
                                    starterTeamId = state.starterTeamId,
                                    onWinnerSelected = { winnerId ->
                                        // بستن شیت و ارسال برنده به ویومدل
                                        scope.launch {
                                            sheetState.hide()

                                            viewModel.handleIntent(
                                                StartGameIntent.OnOpeningDuelWinner(
                                                    winnerId
                                                )

                                            )
                                        }

                                    }, onDismissRequest = {
                                        viewModel.handleIntent(StartGameIntent.OnDismissDialog)
                                    })
                            }

                            GameDialogState.RoundResult -> {
                                BottomSheetResultOfThisRound(
                                    isTeamOneHavingGoal = state.team1.hasGoal, // تشخیص اینکه متن برای کدوم تیم باشه
                                    onResultClicked = { outcome ->
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnRoundResult(outcome)
                                            )
                                        }
                                    }
                                )
                            }

                            GameDialogState.ShahGoalResult -> {
                                val isTeamOnHavingGoal = state.team1.hasGoal
                                BottomSheetResultShahGoal(
                                    isTeamOne = isTeamOnHavingGoal,
                                    onResult = { isGoalFound ->
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(
                                                StartGameIntent.OnShahGoalResult(
                                                    isGoalFound
                                                )
                                            )
                                        }
                                    }
                                )
                            }

                            GameDialogState.Winner -> {
                                val isTeamOneWinner = state.team1.score > state.team2.score

                                BottomSheetWinner(
                                    isTeamOneWinner = isTeamOneWinner,
                                    onClickRepeatGame = {
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(StartGameIntent.OnRepeatGame)
                                        }
                                    },
                                    onClickExit = {
                                        scope.launch {
                                            sheetState.hide()
                                            viewModel.handleIntent(StartGameIntent.OnExitConfirmed)
                                        }
                                    }
                                )
                            }

                            GameDialogState.None -> {}
                        }

                    }
                }

// CustomToast در بالای صفحه
                if (state.toastMessage != null) {
                    CustomToast(
                        message = state.toastMessage ?: "",
                        isVisible = true,
                        color = if (state.toastType == ToastType.ERROR) Red else Green,
                        icon = if (state.toastType == ToastType.ERROR) R.drawable.danger_circle else R.drawable.check_circle,
                        onDismiss = { }
                    )
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamInfoSection(
    infoTeam: TeamModel,
    oppositeTeamCardsCount: Int,
    enableShahGoal: Boolean,
    emptyHandCount: Int,
    onIntent: (StartGameIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(
            top = paddingTopMedium(),
            start = paddingRound(),
            end = paddingRound(),
            bottom = paddingRound()
        ),
        horizontalArrangement = Arrangement.spacedBy(5.sdp)
    ) {
        // تعداد خالی بازی
        InfoBox(
            isVisibility = true,
            value = emptyHandCount.toString(),
            icon = R.drawable.hand,
            label = stringResource(R.string.empty_game),
            onClickItem = {
                onIntent(StartGameIntent.OnEmptyHandClicked)
            }
        )
        // تعداد کارت‌ها
        InfoBox(
            isVisibility = !enableShahGoal,
            value = oppositeTeamCardsCount.toString(),
            icon = R.drawable.icon_card,
            label = stringResource(R.string.cards),
            onClickItem = {
                onIntent(StartGameIntent.OnCardsItemClicked)
            }
        )
        // تعداد مکعب
        InfoBox(
            isVisibility = !enableShahGoal,
            value = infoTeam.numberCubes.toString(),
            icon = R.drawable.cube,
            label = stringResource(R.string.cube),
            onClickItem = {
                onIntent(StartGameIntent.OnCubeItemClicked)
            }
        )
    }
}

@Composable
fun InfoBox(
    isVisibility: Boolean = true,
    modifier: Modifier = Modifier,
    value: String,
    icon: Int,
    label: String,
    onClickItem: () -> Unit
) {
    Column(
        modifier = modifier
            .size(75.sdp)
            .background(
                color = FenceGreen,
                shape = RoundedCornerShape(sizeRound())
            )
            .alpha(if (isVisibility) 1f else .5f)
            .padding(8.sdp)
            .clickable(enabled = isVisibility) { onClickItem() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            text = value,
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White
        )

        Icon(
            painter = painterResource(icon),
            contentDescription = "time",
            modifier = modifier.size(
                sizePicVerySmall()
            ), tint = Color.White
        )
        Text(
            text = label,
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TableGame(
    modifier: Modifier = Modifier,
    whichTeamHasGoal: Int,
    remainingTime: Int,
    showTimer: Boolean,
    isVisibilityShahGoal: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(320.sdp)
            .padding(paddingRound()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha()),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painterResource(R.drawable.person_one),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
            Image(
                painterResource(R.drawable.person_two),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
            Image(
                painterResource(R.drawable.person_three),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
        }
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxHeight()) {
            Image(
                painter = painterResource(R.drawable.table),
                contentDescription = "table",
                modifier = modifier.width(135.sdp),
                contentScale = ContentScale.FillWidth
            )
            if (isVisibilityShahGoal) {
                Text(
                    modifier = modifier.padding(bottom = 100.sdp),
                    text = stringResource(R.string.shah_goal),
                    fontSize = titleSize(),
                    fontFamily = FontPeydaBold,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = modifier.width(100.sdp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontFamily = FontPeydaBold)) { // فونت پیش‌فرض برای اعداد
                        append(
                            if (showTimer) String.format(
                                Locale.US,
                                "%02d:%02d",
                                remainingTime / 60,
                                remainingTime % 60
                            ) else ""
                        )
                    }
                },
                fontSize = titleSize(),
                fontFamily = FontPeydaBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha()),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painterResource(R.drawable.person_four),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
            Image(
                painterResource(R.drawable.person_five),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
            Image(
                painterResource(R.drawable.person_six),
                contentDescription = null,
                modifier = modifier.size(
                    sizePicMedium_two()
                )
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TableDuel(
    modifier: Modifier = Modifier,
    whichTeamHasGoal: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(320.sdp)
            .padding(paddingRound()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = modifier.alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha()),
            painter = painterResource(R.drawable.line),
            contentDescription = "line",
            tint = Color.White
        )
        Image(
            painterResource(R.drawable.person_three),
            contentDescription = null,
            modifier = modifier
                .size(
                    sizePicMedium_two()
                )
                .alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha())
        )
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxHeight()) {
            Image(
                painter = painterResource(R.drawable.table),
                contentDescription = "table",
                modifier = modifier.width(135.sdp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = stringResource(R.string.duel),
                fontSize = titleSize(),
                fontFamily = FontPeydaBold,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
        Image(
            painterResource(R.drawable.person_six),
            contentDescription = null,
            modifier = modifier
                .size(
                    sizePicMedium_two()
                )
                .alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha())
        )
        Icon(
            modifier = modifier.alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha()),
            painter = painterResource(R.drawable.line),
            contentDescription = "line",
            tint = Color.White
        )
    }
}

@Composable
fun HeaderGame(
    modifier: Modifier = Modifier,
    duel: Boolean,
    scoreTeamTwo: Int,
    whichTeamHasGoal: Int,
    scoreTeamOne: Int
) {
    Row(
        modifier = modifier
            .padding(paddingRound())
            .fillMaxWidth()
            .height(50.sdp)
            .background(color = FenceGreen, shape = RoundedCornerShape(sizeRound()))
            .alpha(if (duel) mediumAlpha() else 1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(R.drawable.pic_team_one),
            contentDescription = "pic_one",
            modifier = modifier
                .size(
                    sizePicSmall()
                )
                .alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha())
        )
        Text(
            modifier = modifier.alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha()),
            text = stringResource(R.string.team_one),
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White
        )
        Text(
            modifier = modifier.alpha(if (whichTeamHasGoal == 0) 1f else mediumAlpha()),
            text = "$scoreTeamOne",
            fontSize = descriptionSize(),
            fontFamily = FontPeydaBold,
            color = Color.White
        )
        VerticalDivider(modifier.height(25.sdp), thickness = 2.sdp)
        Text(
            modifier = modifier.alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha()),
            text = "$scoreTeamTwo",
            fontSize = descriptionSize(),
            fontFamily = FontPeydaBold,
            color = Color.White
        )
        Text(
            modifier = modifier.alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha()),
            text = stringResource(R.string.team_two),
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White
        )
        Image(
            painter = painterResource(R.drawable.pic_team_two),
            contentDescription = "pic_two",
            modifier = modifier
                .size(
                    sizePicSmall()
                )
                .alpha(if (whichTeamHasGoal == 1) 1f else mediumAlpha())
        )
    }
}