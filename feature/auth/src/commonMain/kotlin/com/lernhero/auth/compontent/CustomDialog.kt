package com.lernhero.auth.compontent

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes.Companion.Circle
import androidx.compose.material3.MaterialShapes.Companion.PixelCircle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.graphics.shapes.RoundedPolygon
import com.lernhero.auth.AuthViewModel
import com.lernhero.domain.data.CharacterPreset
import com.lernhero.shared.domain.Character
import com.lernhero.shared.FontPixel
import com.lernhero.shared.FontSize
import com.lernhero.shared.Resources
import com.lernhero.shared.SilverDeep
import com.lernhero.shared.StatsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    val viewmodel = koinViewModel<AuthViewModel>()
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            var clickedKnight by remember { mutableStateOf(false) }
            var clickedSorcerer by remember { mutableStateOf(false) }
            var scaleKnight by remember { mutableStateOf(1f) }
            var scaleSorcerer by remember { mutableStateOf(1f) }
            val rememberAnimate by remember { mutableStateOf(true) }

            val animatedScaleKnight by animateFloatAsState(
                targetValue = scaleKnight,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            val animatedScaleSorcerer by animateFloatAsState(
                targetValue = scaleSorcerer,
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            var name by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f) // <-- 80% wysokości ekranu
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        "Create Character",
                        fontSize = FontSize.LARGE,
                        fontFamily = FontPixel()
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {newText -> name = newText},
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Chose your character",

                        fontFamily = FontPixel()
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(100.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .scale(animatedScaleKnight)
                                .weight(1f)
                                .clickable(
                                    onClick = {
                                        clickedKnight = true
                                        clickedSorcerer = false
                                        scaleSorcerer = 0.7f
                                        scaleKnight = 1.5f
                                        viewmodel.toggleAnimate()
                                    }
                                )
                        ) {
                            ChangeableLoadingIndicator(
                                isMoving = clickedKnight,
                                polygons = listOf(
                                    Circle,
                                    PixelCircle
                                ),
                                color = SilverDeep
                            )
                            Image(
                                painter = painterResource(Resources.Image.knightAvatar),
                                contentDescription = "Knight Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .scale(animatedScaleSorcerer)
                                .weight(1f)
                                .clickable(
                                    onClick = {
                                        clickedKnight = false
                                        clickedSorcerer = true
                                        scaleSorcerer = 1.5f
                                        scaleKnight = 0.7f
                                        viewmodel.toggleAnimate()
                                    }
                                )
                        ) {
                            ChangeableLoadingIndicator(
                                isMoving = clickedSorcerer,
                                polygons = listOf(
                                    Circle,
                                    PixelCircle
                                ),
                                color = SilverDeep
                            )
                            Image(
                                painter = painterResource(Resources.Image.sorcererAvatar),
                                contentDescription = "Knight Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    if (clickedKnight || clickedSorcerer){
                        if (clickedKnight){
                            CharacterInfo(CharacterPreset.Knight)
                        }
                        if (clickedSorcerer){
                            CharacterInfo(CharacterPreset.Sorcerer)
                        }
                    }
                }
                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = onDismiss,
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun CharacterInfo(character: Character){
    Column(
    ) {
        StatBar("maxHp",character.maxHp, order = 1)
        Spacer(modifier = Modifier.height(5.dp))
        StatBar("maxMana",character.maxMana, order = 2)
        Spacer(modifier = Modifier.height(5.dp))
        StatBar("defense",character.defense, order = 3)
        Spacer(modifier = Modifier.height(5.dp))
        StatBar("attack",character.attack, order = 4)
        Spacer(modifier = Modifier.height(5.dp))
        StatBar("speed",character.speed, order = 5)



    }

}
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StatBar(
    label: String,
    value: Int,
    maxValue: Int = 70,
    order: Long
){
    val viewmodel = koinViewModel<AuthViewModel>()
    val scope =  rememberCoroutineScope()
    val animateProgress by viewmodel.uiState.collectAsState()


    var rememberFloat by remember { mutableStateOf(0f) }


    LaunchedEffect(animateProgress){
        scope.launch {
            rememberFloat = 0f
            delay(order*150)
            rememberFloat = value/maxValue.toFloat()
        }

    }
    val animatedProgress by animateFloatAsState(
        rememberFloat,
        animationSpec = tween(1500)
    )
    val color =  StatsHelper.colorMap[label]
    val icon = StatsHelper.iconMap[label]


    Row(modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(20.dp)
    )  {
        Icon(
            modifier = Modifier.fillMaxHeight(),
            painter = painterResource(icon!!),
            contentDescription = "icon"

        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$value",
            fontSize = FontSize.SMALL
        )
        Spacer(modifier = Modifier.width(5.dp))
        LinearWavyProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = {animatedProgress},
            color = color!!,
            trackColor = Color.Gray,


        )

    }

}

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ChangeableLoadingIndicator(
    isMoving: Boolean,
    polygons: List<RoundedPolygon>,
    color: Color
){

    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    val progress = if (isMoving) animatedProgress else 0f

    LoadingIndicator(
        modifier = Modifier.fillMaxSize(),
        progress = {progress},
        polygons = polygons,
        color = color
    )

}
//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
//@Composable
//fun MorphingBox(
//    from: androidx.graphics.shapes.RoundedPolygon,
//    to: androidx.graphics.shapes.RoundedPolygon,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Blue,
//    durationMillis: Int = 2000,
//    content: @Composable () -> Unit = {}
//) {
//    val morph = remember { Morph(from, to) }
//
//    val infiniteTransition = rememberInfiniteTransition(label = "morphAnim")
//    val progress by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "progress"
//    )
//
//    Box(
//        modifier = modifier
//            .clip(MorphShape(morph, progress))
//            .background(color)
//
//    ) {
//        content()
//    }
//}



//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
//class MorphShape(
//    private val morph: Morph,
//    private val progress: Float
//) : Shape {
//    override fun createOutline(
//        size: Size,
//        layoutDirection: LayoutDirection,
//        density: Density
//    ): Outline {
//        // Path z morphowania (0..1 space)
//        val rawPath = morph.toPath(progress)
//
//        // Skopiowany path do dalszych operacji
//        val path = Path().apply { addPath(rawPath) }
//
//        // Skalowanie do rozmiaru kontenera
//        val matrix = Matrix().apply { scale(size.width, size.height) }
//        path.transform(matrix)
//
//        // Wyśrodkowanie w kontenerze
//        path.translate(size.center - path.getBounds().center)
//
//        return Outline.Generic(path)
//    }
//}



