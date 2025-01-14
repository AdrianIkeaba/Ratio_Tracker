package com.ghostdev.tracker.cal.ai.presentation.home.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.food_placeholder
import coil3.compose.AsyncImage
import com.ghostdev.tracker.cal.ai.models.Recipe
import com.ghostdev.tracker.cal.ai.utilities.SystemService
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class RecipeDetailsComponent(
    private val recipe: Recipe
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            RecipeDetailsContent(navigator, recipe)
        }
    }
}

@Composable
private fun RecipeDetailsContent(
    navigator: Navigator?,
    recipe: Recipe,
    shareService: SystemService = koinInject()
) {
    val scrollState = rememberScrollState()

    val proteins = recipe.totalNutrients["PROCNT"]?.quantity?.toInt()
    val fats = recipe.totalNutrients["FAT"]?.quantity?.toInt()
    val carbs = recipe.totalNutrients["CHOCDF"]?.quantity?.toInt()
    val calories = recipe.totalNutrients["ENERC_KCAL"]?.quantity?.toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = recipe.image,
                    placeholder = painterResource(Res.drawable.food_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Icon(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(start = 16.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable(
                            onClick = { navigator?.pop() }
                        )
                        .size(30.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = recipe.label,
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$calories Cal",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                shareService.openUrl(recipe.shareAs)
                            },
                        imageVector = Icons.Default.Info,
                        contentDescription = "More Info",
                        tint = Color.LightGray
                    )
                }

                // Rest of the content remains the same
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            ) {
                                append("$calories\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            ) {
                                append("Calories")
                            }
                        },
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            ) {
                                append("${proteins}g\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            ) {
                                append("Proteins")
                            }
                        },
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            ) {
                                append("${fats}g\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            ) {
                                append("Fats")
                            }
                        },
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            ) {
                                append("${carbs}g\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            ) {
                                append("Carbs")
                            }
                        },
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                )

                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = "Ingredients: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )

                for (ingredient in recipe.ingredientLines) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = ingredient,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}