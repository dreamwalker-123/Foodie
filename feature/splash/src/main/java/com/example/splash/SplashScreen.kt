package com.example.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun SplashScreen(
    popBackStack: () -> Unit,
    navigate: () -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("anim.json")
    )
    val progress by animateLottieCompositionAsState(composition = composition)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF15412))) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxWidth(),
            enableMergePaths = true,
            progress = { progress }
        )
    }

    LaunchedEffect(key1 = progress) {
        if (progress >= .5f) {
            popBackStack()
            navigate()
        }
    }

}