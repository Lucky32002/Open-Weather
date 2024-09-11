package uk.ac.tees.mad.d3896530.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.R

@Composable
fun SplashScreen(onComplete: () -> Unit) {

    val scale by remember {
        mutableStateOf(Animatable(0f))
    }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
        delay(3000)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.LightGray.copy(0.1f),
                        Color.LightGray
                    )
                )
            )
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.openweather),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
                .clip(CircleShape)
                .scale(scale.value)
        )
        Text(
            text = "Weather at your fingertips",
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

object SplashDes : NavDestinations {
    override val navRoute = "splash"
}