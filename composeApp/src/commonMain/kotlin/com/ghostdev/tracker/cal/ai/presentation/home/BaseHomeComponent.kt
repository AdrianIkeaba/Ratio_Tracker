package com.ghostdev.tracker.cal.ai.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.diary_selected
import cal.composeapp.generated.resources.recipes_selected
import cal.composeapp.generated.resources.reports_selected
import com.ghostdev.tracker.cal.ai.presentation.home.components.NavBarItem
import com.ghostdev.tracker.cal.ai.presentation.home.diary.DiaryComponentInit
import com.ghostdev.tracker.cal.ai.presentation.home.recipes.RecipesComponentInit
import com.ghostdev.tracker.cal.ai.presentation.home.report.ReportsComponentInit
import org.jetbrains.compose.resources.painterResource

object RecipesComponent: Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            RecipesComponentInit(navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.recipes_selected)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Recipes",
                    icon = icon
                )
            }
        }
}


object HomeComponent : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DiaryComponentInit(
                navigator = navigator
            )
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.diary_selected)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Diary",
                    icon = icon
                )
            }
        }
}


object ReportsComponent: Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
        ) {
            ReportsComponentInit(navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.reports_selected)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Reports",
                    icon = icon
                )
            }
        }
}

class BaseHomeComponent : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeComponent) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        containerColor = Color(0xFFDBFBED)
                    ) {
                        NavBarItem(tab = RecipesComponent)
                        NavBarItem(tab = HomeComponent)
                        NavBarItem(tab = ReportsComponent)
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }
}


