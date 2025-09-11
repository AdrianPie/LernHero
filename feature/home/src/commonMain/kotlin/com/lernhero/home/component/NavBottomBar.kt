package com.lernhero.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lernhero.home.domain.BottomBarDestination
import com.lernhero.shared.IconPrimary
import com.lernhero.shared.IconSecondary
import com.lernhero.shared.SurfaceLighter
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavBottomBar(
    modifier: Modifier,
    selected : Boolean,
    onSelect: (BottomBarDestination) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12))
        .background(SurfaceLighter)
        .padding(
            vertical = 24.dp,
            horizontal = 36.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val animatedColor by animateColorAsState(
                targetValue = if (selected) IconSecondary else IconPrimary
            )
            IconButton(
                onClick = {onSelect(destination)},
                content = {
                    Icon(
                        painter = painterResource(destination.icon),
                        tint = animatedColor,
                        contentDescription = "Bottom bar icon"
                    )
                }
            )


        }
    }

}