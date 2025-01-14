package com.ghostdev.tracker.cal.ai.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.clock
import com.ghostdev.tracker.cal.ai.models.Meal
import org.jetbrains.compose.resources.painterResource


@Composable
fun MealsItem(
    mealType: String,
    calories: String,
    time: String,
    meal: Meal,
    onMealClick: (Meal) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onMealClick(meal) }
            )
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF7F7F7)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = mealType,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                Row {
                  Icon(
                      modifier = Modifier
                          .size(18.dp)
                          .clickable(
                              onClick = {
                                  TODO()
                              }
                          ),
                      painter = painterResource(Res.drawable.clock),
                      contentDescription = null
                  )

                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                    )

                    Text(
                        text = time,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "$calories Cal",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    Spacer(
        modifier = Modifier
            .height(18.dp)
    )
}