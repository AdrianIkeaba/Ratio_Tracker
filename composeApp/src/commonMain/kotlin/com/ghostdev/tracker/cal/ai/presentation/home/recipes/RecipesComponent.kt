package com.ghostdev.tracker.cal.ai.presentation.home.recipes

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.banana
import cal.composeapp.generated.resources.food_placeholder
import cal.composeapp.generated.resources.search
import coil3.compose.AsyncImage
import com.ghostdev.tracker.cal.ai.models.RecipeResponse
import com.ghostdev.tracker.cal.ai.presentation.base.BaseLoadingComposable
import com.ghostdev.tracker.cal.ai.presentation.home.profile.ProfileComponent
import com.ghostdev.tracker.cal.ai.utilities.SystemService
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipesComponentInit(
    navigator: Navigator?,
    viewmodel: RecipesLogic = koinViewModel()
) {
    val state by viewmodel.recipeState.collectAsState()
    val searchQuery by viewmodel.searchQuery.collectAsState()

    state.recipes.let { ready ->
        RecipesContent(
            navigator = navigator,
            recipes = ready,
            userName = state.userName,
            searchQuery = searchQuery,
            onSearch = { query ->
                viewmodel.getRecipes(query)
            },
            loading = state.loading
        )
    }
}

@Composable
private fun RecipesContent(
    navigator: Navigator?,
    recipes: RecipeResponse?,
    userName: String? = null,
    searchQuery: String,
    onSearch: (String) -> Unit = {},
    loading: Boolean = false
) {
    val navBarHeight = 70.dp
    val recipeSearchText = remember { mutableStateOf(searchQuery) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = navBarHeight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navigator?.parent?.push(ProfileComponent())
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .size(45.dp),
                        shape = CircleShape,
                        backgroundColor = Color.Black
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            painter = painterResource(Res.drawable.banana),
                            contentDescription = "Profile Picture",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = userName ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    value = recipeSearchText.value,
                    singleLine = true,
                    onValueChange = { recipeSearchText.value = it},
                    placeholder = { Text(text = "Search for recipes...") },
                    leadingIcon = { Icon(painter = painterResource(Res.drawable.search),
                        contentDescription = null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF49D199),
                        cursorColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (recipeSearchText.value.isNotEmpty()) {
                                onSearch(recipeSearchText.value)
                            }
                        }
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                if (loading) {
                    BaseLoadingComposable()
                } else {
                    if (recipes?.hits.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                text = "No recipes yet.",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    } else {
                        LazyColumn(
                            state = persistedLazyScrollState()
                        ) {
                            items(recipes!!.hits.size) {
                                RecipeItem(
                                    imageUrl = recipes.hits[it].recipe.image,
                                    recipeName = recipes.hits[it].recipe.label,
                                    recipeCalories = recipes.hits[it].recipe.calories.toInt()
                                        .toString(),
                                    shareLink = recipes.hits[it].recipe.shareAs,
                                    onItemClicked = {
                                        navigator?.parent?.push(
                                            RecipeDetailsComponent(
                                                recipes.hits[it].recipe
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    imageUrl: String,
    recipeName: String,
    recipeCalories: String,
    shareLink: String,
    onItemClicked: () -> Unit,
    shareService: SystemService = koinInject(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onItemClicked),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AsyncImage(
                model = imageUrl,
                placeholder = painterResource(Res.drawable.food_placeholder),
                contentDescription = recipeName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
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
                        text = recipeName,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$recipeCalories Cal",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = { shareService.shareRecipe(shareLink) }),
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.LightGray,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun persistedLazyScrollState(viewModel: RecipesLogic = koinViewModel()): LazyListState {
    val scrollState = rememberLazyListState(viewModel.firstVisibleItemIdx, viewModel.firstVisibleItemOffset)
    DisposableEffect(key1 = null) {
        onDispose {
            viewModel.saveScrollPosition(
                scrollState.firstVisibleItemIndex,
                scrollState.firstVisibleItemScrollOffset
            )
        }
    }
    return scrollState
}
