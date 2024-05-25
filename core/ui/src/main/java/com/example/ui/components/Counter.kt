package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.R

@Composable
fun Counter(
    amount: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevated: Boolean = true
) {
    val containerColor = if (elevated) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val elevation = if (elevated) {
        FloatingActionButtonDefaults.elevation()
    } else {
        FloatingActionButtonDefaults.loweredElevation(defaultElevation = 0.dp)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SmallFloatingActionButton(
            onClick = onMinusClick,
            shape = MaterialTheme.shapes.small,
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = elevation
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = stringResource(R.string.button_minus_description)
            )
        }
        AnimatedContent(
            targetState = amount,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut())
                } else {
                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> height } + fadeOut())
                }.using(SizeTransform(clip = false))
            }, label = ""
        ) { targetCount ->
            Text(
                text = "$targetCount",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
        SmallFloatingActionButton(
            onClick = onPlusClick,
            shape = MaterialTheme.shapes.small,
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = elevation
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(R.string.button_plus_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterPreview() {
//    FoodiesTheme {
        Counter(
            amount = 1,
            onMinusClick = {},
            onPlusClick = {},
            elevated = false
        )
//    }
}