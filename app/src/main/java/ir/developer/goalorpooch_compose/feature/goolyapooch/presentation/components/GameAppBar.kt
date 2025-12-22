package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.theme.FontPeydaBold
import ir.developer.goalorpooch_compose.core.theme.White
import ir.developer.goalorpooch_compose.core.theme.paddingRound
import ir.developer.goalorpooch_compose.core.theme.paddingTop
import ir.developer.goalorpooch_compose.core.theme.titleSize

@Composable
fun GameAppBar(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = paddingRound(),
                    start = paddingRound(),
                    end = paddingRound()
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontSize = titleSize(),
                fontFamily = FontPeydaBold,
                color = White
            )

            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back",
                        tint = White
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                top = paddingTop(),
                start = paddingRound(),
                end = paddingRound()
            ),
            color = White.copy(alpha = 0.5f)
        )
    }
}