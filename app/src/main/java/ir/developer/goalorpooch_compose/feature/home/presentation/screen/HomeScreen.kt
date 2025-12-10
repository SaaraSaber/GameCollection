package ir.developer.goalorpooch_compose.feature.home.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.ChampionBlue
import ir.developer.goalorpooch_compose.core.theme.DarkBackground
import ir.developer.goalorpooch_compose.core.theme.MiamiBlue
import ir.developer.goalorpooch_compose.core.theme.Rose
import ir.developer.goalorpooch_compose.core.theme.White
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Scaffold() {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = DarkBackground)
        ) {
            HomeTopBar(coinBalance = 0, onExitClick = {}, onInfoClick = {})
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
            .padding(8.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(12.sdp)
                    .background(color = ChampionBlue, shape = RoundedCornerShape(2.sdp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {}, modifier = modifier.size(8.sdp)) {
                    Icon(
                        painter = painterResource(R.drawable.exit_icon),
                        contentDescription = null,
                        tint = Rose
                    )
                }
            }

            Box(
                modifier = modifier
                    .size(12.sdp)
                    .background(color = ChampionBlue, shape = RoundedCornerShape(2.sdp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {}, modifier = modifier.size(8.sdp)) {
                    Icon(
                        painter = painterResource(R.drawable.info_circle),
                        contentDescription = null,
                        tint = MiamiBlue
                    )
                }
            }
        }

        Box(
            modifier = modifier.background(
                color = ChampionBlue,
                shape = RoundedCornerShape(2.sdp)
            ).padding(top = 2.sdp, bottom = 2.sdp, start = 6.sdp, end = 6.sdp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.coin_icon),
                    contentDescription = null,
                    modifier = modifier.size(10.sdp)
                )
                Text(text = "0", color = White, fontSize = 12.ssp)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

    HomeScreen()
}