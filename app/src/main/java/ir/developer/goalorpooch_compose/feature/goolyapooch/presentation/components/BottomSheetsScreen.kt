package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import ir.developer.goalorpooch_compose.core.theme.paddingRoundMini
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.paddingTopLarge
import ir.developer.goalorpooch_compose.core.theme.paddingTopMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicLarge
import ir.developer.goalorpooch_compose.core.theme.sizePicMedium
import ir.developer.goalorpooch_compose.core.theme.sizePicSmall
import ir.developer.goalorpooch_compose.core.theme.sizeRound
import ir.developer.goalorpooch_compose.core.theme.titleSize
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.DuelResult
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.RoundOutcome
import ir.kaaveh.sdpcompose.sdp

@Composable
fun BottomSheetOpeningDuel(
    modifier: Modifier = Modifier,
    starterTeamId: Int,
    onWinnerSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val isTeamOneStarter = starterTeamId == 0
    val opponentTeamId = if (isTeamOneStarter) 1 else 0
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.note),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = modifier.padding(end = 5.sdp)
                )
                Text(
                    text = stringResource(R.string.opening_duel),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
            }
            HorizontalDivider(modifier = modifier.padding(top = paddingTop()))

            Text(
                modifier = modifier.padding(top = paddingTop(), bottom = paddingTopMedium()),
                text = stringResource(R.string.description_opening_duel),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )
            OpeningDuelItem(
                image = if (isTeamOneStarter) R.drawable.pic_team_one else R.drawable.pic_team_two,
                text = if (isTeamOneStarter)
                    stringResource(R.string.first_team_kept_goal_)
                else
                    stringResource(R.string.second_team_kept_goal_),
                onClick = {
                    // اگر حفظ کرد، یعنی برنده همون تیم شروع‌کننده است
                    onWinnerSelected(starterTeamId)
                    onDismissRequest()
                }
            )

            Spacer(modifier = Modifier.height(8.sdp))

            // --- گزینه ۲: حریف گل را گرفت ---
            OpeningDuelItem(
                image = if (isTeamOneStarter) R.drawable.pic_team_two else R.drawable.pic_team_one,
                text = if (isTeamOneStarter)
                    stringResource(R.string.second_team_scored) // تیم دوم گرفت
                else
                    stringResource(R.string.first_team_scored), // تیم اول گرفت
                onClick = {
                    // اگر گل گرفته شد، یعنی برنده تیم حریف است
                    onWinnerSelected(opponentTeamId)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun OpeningDuelItem(
    @DrawableRes image: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(sizeRound()))
            .border(1.sdp, Color.White, RoundedCornerShape(sizeRound()))
            .clickable { onClick() }
            .padding(vertical = 8.sdp, horizontal = paddingRound()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(sizePicMedium()),
            painter = painterResource(id = image),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.sdp))

        Text(
            text = text,
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun BottomSheetWinner(
    modifier: Modifier = Modifier,
    isTeamOneWinner: Boolean,
    onClickExit: () -> Unit,
    onClickRepeatGame: () -> Unit
) {
    val winnerImage = if (isTeamOneWinner) R.drawable.pic_team_one else R.drawable.pic_team_two
    val winnerDescription = if (isTeamOneWinner)
        R.string.description_final_game_team_one
    else
        R.string.description_final_game_team_two

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.final_result),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
            }
            HorizontalDivider(modifier = modifier.padding(top = paddingTop()))

            Image(
                modifier = modifier
                    .padding(paddingTopMedium())
                    .size(sizePicLarge()),
                painter = painterResource(winnerImage),
                contentDescription = null
            )

            Text(
                modifier = modifier.padding(top = paddingTop(), bottom = paddingTopMedium()),
                text = stringResource(winnerDescription),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = titleSize(),
                textAlign = TextAlign.Center
            )

            Row(modifier = Modifier.padding(top = paddingTopMedium())) {
                Button(
                    modifier = Modifier
                        .padding(end = 5.sdp)
                        .height(heightButton())
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = FenceGreen
                    ),
                    onClick = { onClickRepeatGame() }
                ) {
                    Text(
                        text = stringResource(R.string.repeat_game),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = FenceGreen
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(start = 5.sdp)
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

@Composable
fun BottomSheetContactResultDuel(
    modifier: Modifier = Modifier,
    isTeamOneHolder: Boolean, // ✅ تیمی که الان گل دستشه (درگیر دوئل)
    onResult: (DuelResult) -> Unit
) {
    val teamImage = if (isTeamOneHolder) R.drawable.pic_team_one else R.drawable.pic_team_two

    // تعیین متن‌ها (بهتر است به string.xml ببرید)
    val textKept = if (isTeamOneHolder) "تیم اول گل را حفظ کرد." else "تیم دوم گل را حفظ کرد."
    val textLost = if (isTeamOneHolder) "تیم اول گل را از دست داد." else "تیم دوم گل را از دست داد."

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.note),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = modifier.padding(end = 5.sdp)
                )
                Text(
                    text = stringResource(R.string.result_duel),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
            }
            HorizontalDivider(
                modifier = modifier.padding(
                    top = paddingTop(),
                    bottom = paddingTopMedium()
                )
            )

            DuelOptionItem(
                text = textKept,
                image = teamImage,
                onClick = { onResult(DuelResult.KEPT_GOAL) }
            )

            Spacer(modifier = Modifier.height(8.sdp))

            // --- گزینه ۲: از دست دادن گل (شکست) ---
            DuelOptionItem(
                text = textLost,
                image = teamImage,
                onClick = { onResult(DuelResult.LOST_GOAL) }
            )
        }
    }
}

@Composable
fun DuelOptionItem(
    text: String,
    @DrawableRes image: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(sizeRound()))
            .border(1.sdp, Color.White, RoundedCornerShape(sizeRound()))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(paddingRound())
                .size(sizePicMedium()),
            painter = painterResource(id = image),
            contentDescription = null
        )

        Text(
            text = text,
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun BottomSheetResultShahGoal(
    modifier: Modifier = Modifier,
    isTeamOne: Boolean,
    onResult: (isGoalFound: Boolean) -> Unit
) {
    val successText =
        if (isTeamOne) R.string.first_team_scored_shah_goal else R.string.second_team_scored_shah_goal
    val failureText =
        if (isTeamOne) R.string.first_team_did_not_scored_shah_goal else R.string.second_team_did_not_scored_shah_goal

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.result_of_shah_goal),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
                Spacer(modifier = modifier.weight(1f))
            }
            HorizontalDivider(
                modifier = modifier.padding(
                    top = paddingTop(),
                    bottom = paddingTop()
                )
            )

            ItemResult(
                modifier = modifier,
                icon = R.drawable.tick,
                text = stringResource(successText),
                onClick = { onResult(false) }
            )
            ItemResult(
                modifier = modifier,
                icon = R.drawable.square_cross,
                text = stringResource(failureText),
                onClick = { onResult(true) }
            )
        }
    }
}

@Composable
fun ItemResult(
    modifier: Modifier,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = paddingTop())
            .border(1.sdp, Color.White, RoundedCornerShape(sizeRound()))
            .clip(RoundedCornerShape(sizeRound()))
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(paddingTopMedium())
                .size(sizePicSmall()),
            painter = painterResource(id = icon),
            contentDescription = null
        )

        Text(
            text = text,
            fontSize = descriptionSize(),
            fontFamily = FontPeydaMedium,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun BottomSheetResultOfThisRound(
    modifier: Modifier = Modifier,
    isTeamOneHavingGoal: Boolean,
    onResultClicked: (RoundOutcome) -> Unit
) {
    val technicalWinText =
        if (!isTeamOneHavingGoal) R.string.first_team_scored_a_single_goal else R.string.second_team_scored_a_single_goal
    val simpleWinText =
        if (!isTeamOneHavingGoal) R.string.first_team_scored else R.string.second_team_scored
    val lossText =
        if (!isTeamOneHavingGoal) R.string.first_team_did_not_score else R.string.second_team_did_not_score

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.result_of_this_round),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
                Spacer(modifier = modifier.weight(1f))
            }
            HorizontalDivider(
                modifier = modifier.padding(
                    top = paddingTop(),
                    bottom = paddingTop()
                )
            )

            ItemResult(
                modifier = modifier,
                icon = R.drawable.cup,
                text = stringResource(technicalWinText),
                onClick = { onResultClicked(RoundOutcome.TAK_ZARB) }
            )

//            Spacer(modifier = Modifier.height(12.sdp))

            // --- گزینه ۲: برد ساده/حفظ گل (تیک) ---
            ItemResult(
                modifier = modifier,
                icon = R.drawable.tick,
                text = stringResource(simpleWinText),
                onClick = { onResultClicked(RoundOutcome.TOOK_GOAL) }
            )

//            Spacer(modifier = Modifier.height(12.sdp))

            // --- گزینه ۳: باخت/پوچ (ضربدر) ---
            ItemResult(
                modifier = modifier,
                icon = R.drawable.square_cross,
                text = stringResource(lossText),
                onClick = { onResultClicked(RoundOutcome.DID_NOT_TAKE) }
            )

//            Spacer(modifier = Modifier.height(16.sdp))
        }
    }
}

@Composable
fun BottomSheetCards(
    modifier: Modifier = Modifier,
    availableCards: List<GameCardModel>,
    targetTeamId: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var selectedCardId by remember { mutableStateOf<Int?>(null) }
    val teamImage = if (targetTeamId == 0) R.drawable.pic_team_one else R.drawable.pic_team_two
    val descriptionText =
        if (targetTeamId == 0) R.string.description_choose_card_team_one else R.string.description_choose_card_team_two

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_card),
                    contentDescription = null,
                )
                Text(
                    modifier = modifier.padding(start = paddingRoundMini()),
                    text = stringResource(R.string.choose_card),
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
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(paddingRoundMini())
            ) {
                Image(
                    painter = painterResource(teamImage),
                    contentDescription = null,
                    modifier = modifier.size(
                        sizePicSmall()
                    )
                )
                Text(
                    modifier = modifier.padding(
                        top = paddingTopMedium(),
                        bottom = paddingTopMedium()
                    ),
                    text = stringResource(descriptionText),
                    color = Color.White,
                    fontFamily = FontPeydaMedium,
                    fontSize = descriptionSize(),
                    textAlign = TextAlign.Justify
                )
            }

            LazyColumn(modifier = modifier.weight(1f)) {
                items(availableCards) { itemCard ->
//                    val isSelected = selectedCardId == itemCard.id
                    ItemCard(
                        card = itemCard,
                        alpha = if (selectedCardId == null || selectedCardId == itemCard.id) 1f else .5f,
                        onClickItem = {
                            selectedCardId = itemCard.id
                        }
                    )
                }
            }

            Row(modifier = Modifier.padding(top = paddingTop())) {
                Button(
                    modifier = Modifier
                        .padding(end = 5.sdp)
                        .height(heightButton())
                        .weight(1f)
                        .alpha(if (selectedCardId == null) 0.5f else 1f), // تغییر آلفا بر اساس مقدار idCard,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        disabledContainerColor = Color.White
                    ),
                    enabled = selectedCardId != null,
                    onClick = {
                        selectedCardId?.let { onConfirm(it) }
                    }
                ) {
                    Text(
                        modifier = modifier.alpha(if (selectedCardId == null) 0.5f else 1f), // تغییر آلفا بر اساس مقدار idCard,,
                        text = stringResource(R.string.yes),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = FenceGreen
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(start = 5.sdp)
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
}

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    card: GameCardModel,
    alpha: Float = 1f,
    onClickItem: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = modifier
                .padding(top = paddingRoundMini())
                .clickable {
                    onClickItem()
                }
                .alpha(alpha),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(sizeRound()),
            border = BorderStroke(width = 1.sdp, color = Color.White)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(paddingRoundMini()),
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
            }
        }
    }
}

@Composable
fun BottomSheetCube(
    modifier: Modifier = Modifier,
    isTeamOne: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    // State برای نگه داشتن انتخاب فعلی
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    // گزینه‌های مکعب
    val options = listOf(2, 4, 6)
// تعیین منابع بر اساس تیم
    val teamImage = if (isTeamOne) R.drawable.pic_team_one else R.drawable.pic_team_two
    val descriptionText =
        if (isTeamOne) R.string.description_score_cube_team_one else R.string.description_score_cube_team_two

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(teamImage),
                    contentDescription = null,
                    modifier = modifier.size(
                        sizePicSmall()
                    )
                )
                Text(
                    modifier = modifier.padding(start = paddingRoundMini()),
                    text = stringResource(R.string.score_cube),
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
                modifier = modifier.padding(top = paddingTopMedium(), bottom = paddingTopMedium()),
                text = stringResource(descriptionText),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )
            // نمایش گزینه‌ها
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(top = paddingTopMedium())
                    .fillMaxWidth()
            ) {
                options.forEach { option ->
                    CubeOptionCard(
                        option = option,
                        isSelected = selectedOption == option,
                        onClick = { selectedOption = option }
                    )
                }
            }
            Row(modifier = Modifier.padding(top = paddingTopLarge())) {
                Button(
                    modifier = Modifier
                        .padding(end = 5.sdp)
                        .height(heightButton())
                        .weight(1f)
                        .alpha(if (selectedOption != null) 1f else .5f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        disabledContainerColor = Color.White
                    ),
                    enabled = selectedOption != null,
                    onClick = {
                        selectedOption?.let {
                            onConfirm(it)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = FenceGreen
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(start = 5.sdp)
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
}

@Composable
fun CubeOptionCard(
    option: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = paddingRoundMini())
            .clickable { onClick() }
            .graphicsLayer(alpha = if (isSelected) 1f else 0.5f), // شفافیت برای انتخاب نشده‌ها
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        shape = RoundedCornerShape(sizeRound()),
        border = BorderStroke(width = 1.sdp, color = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = paddingRoundMini(),
                horizontal = paddingTopMedium()
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.cube),
                contentDescription = null,
                modifier = Modifier.size(sizePicSmall())
            )
            Text(
                modifier = Modifier.padding(top = paddingTop()),
                text = option.toString(),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = titleSize()
            )
        }
    }
}

@Composable
fun BottomSheetConfirmCube(
    modifier: Modifier = Modifier,
    isRequestingTeamOne: Boolean,
    score: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val teamImage = if (isRequestingTeamOne) R.drawable.pic_team_one else R.drawable.pic_team_two

    // متن: "تیم X درخواست مکعب Y امتیازی دارد"
    val descriptionRes = if (isRequestingTeamOne)
        R.string.description_confirm_score_cube_team_one
    else
        R.string.description_confirm_score_cube_team_two
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .padding(start = paddingRound(), end = paddingRound(), bottom = paddingRound())
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(teamImage),
                    contentDescription = null,
                    modifier = modifier.size(
                        sizePicSmall()
                    )
                )
                Text(
                    modifier = modifier.padding(start = paddingRoundMini()),
                    text = stringResource(R.string.confirmation),
                    color = Color.White,
                    fontFamily = FontPeydaBold,
                    fontSize = titleSize()
                )
                Spacer(modifier = modifier.weight(1f))
            }

            HorizontalDivider(modifier = modifier.padding(top = paddingTop()))

            Text(
                modifier = modifier.padding(top = paddingTopMedium(), bottom = paddingTopMedium()),
                text = stringResource(descriptionRes, score),
                color = Color.White,
                fontFamily = FontPeydaMedium,
                fontSize = descriptionSize(),
                textAlign = TextAlign.Justify
            )
            Row(modifier = Modifier.padding(top = paddingTopLarge(), bottom = paddingRound())) {
                Button(
                    modifier = Modifier
                        .padding(end = 5.sdp)
                        .height(heightButton())
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = FenceGreen
                    ),
                    onClick = { onConfirm() }
                ) {
                    Text(
                        text = stringResource(R.string.accept_request),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = FenceGreen
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(start = 5.sdp)
                        .height(heightButton())
                        .weight(1f),
                    border = BorderStroke(1.sdp, Color.White),
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(R.string.no),
                        fontSize = descriptionSize(),
                        fontFamily = FontPeydaMedium,
                        color = Color.White
                    )
                }
            }

        }
    }
}