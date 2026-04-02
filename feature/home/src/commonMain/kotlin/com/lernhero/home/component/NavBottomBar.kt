package com.lernhero.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.lernhero.home.domain.BottomBarDestination
import com.lernhero.shared.FontFirst
import com.lernhero.shared.FontSize
import com.lernhero.shared.IconPrimary
import com.lernhero.shared.IconSecondary
import com.lernhero.shared.SurfaceLighter
import com.lernhero.shared.SurfaceSecondary
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavBottomBar(
    modifier: Modifier = Modifier,
    selected : BottomBarDestination,
    onSelect: (BottomBarDestination) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(elevation = 16.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(SurfaceLighter)
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomBarDestination.entries.forEach { destination ->
                val isSelected = selected == destination
                
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) IconSecondary else IconPrimary
                )
                
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) SurfaceSecondary.copy(alpha = 0.15f) else SurfaceLighter
                )

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onSelect(destination) }
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(destination.icon),
                        tint = iconColor,
                        contentDescription = destination.title
                    )
                    
                    AnimatedVisibility(visible = isSelected) {
                        Row {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = destination.title,
                                fontFamily = FontFirst(),
                                fontSize = FontSize.MEDIUM,
                                color = iconColor
                            )
                        }
                    }
                }
            }
        }
    }
}