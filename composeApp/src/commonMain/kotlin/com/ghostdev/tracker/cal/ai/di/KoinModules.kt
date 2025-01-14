package com.ghostdev.tracker.cal.ai.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ghostdev.tracker.cal.ai.data.api.KtorMealsApi
import com.ghostdev.tracker.cal.ai.data.api.KtorRecipeApi
import com.ghostdev.tracker.cal.ai.data.api.MealsApi
import com.ghostdev.tracker.cal.ai.data.api.RecipeApi
import com.ghostdev.tracker.cal.ai.data.datasource.RatioDataSource
import com.ghostdev.tracker.cal.ai.data.datasource.RatioDataSourceImpl
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepo
import com.ghostdev.tracker.cal.ai.data.repo.db.DatabaseRepoImpl
import com.ghostdev.tracker.cal.ai.data.repo.nutrients.NutrientsRepo
import com.ghostdev.tracker.cal.ai.data.repo.nutrients.NutrientsRepoImpl
import com.ghostdev.tracker.cal.ai.data.repo.recipe.RecipeRepo
import com.ghostdev.tracker.cal.ai.data.repo.recipe.RecipeRepoImpl
import com.ghostdev.tracker.cal.ai.data.repo.user.UserRepo
import com.ghostdev.tracker.cal.ai.data.storage.InMemoryStorage
import com.ghostdev.tracker.cal.ai.data.storage.Storage
import com.ghostdev.tracker.cal.ai.presentation.authentication.login.LoginLogic
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingLogic
import com.ghostdev.tracker.cal.ai.presentation.home.diary.DiaryLogic
import com.ghostdev.tracker.cal.ai.presentation.home.diary.meals.MealLogic
import com.ghostdev.tracker.cal.ai.presentation.home.profile.ProfileLogic
import com.ghostdev.tracker.cal.ai.presentation.home.recipes.RecipesLogic
import com.ghostdev.tracker.cal.ai.presentation.home.report.ReportsLogic
import com.ghostdev.tracker.cal.ai.presentation.home.profile.calories.CalorieLogic
import com.ghostdev.tracker.cal.ai.utilities.createDataStore
import com.ghostdev.tracker.cal.ai.utilities.dataStoreFileName
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformModule(): Module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Application.Json)
            }
        }
    }
    singleOf(::KtorRecipeApi).bind(RecipeApi::class)
    singleOf(::KtorMealsApi).bind(MealsApi::class)
}

val datastoreModule = module {
    single<DataStore<Preferences>> {
        createDataStore { dataStoreFileName }
    }
    single { UserRepo(get()) }
}

val viewModelModule = module {
    factoryOf(::BaseOnboardingLogic)
    factoryOf(::LoginLogic)
    factoryOf(::DiaryLogic)
    factoryOf(::RecipesLogic)
    factoryOf(::MealLogic)
    factoryOf(::ProfileLogic)
    factoryOf(::ReportsLogic)
    factoryOf(::CalorieLogic)
}

val dataSourceModule = module {
    singleOf(::RatioDataSourceImpl).bind(RatioDataSource::class)
}

val repositoryModule  = module {
    singleOf(::DatabaseRepoImpl).bind(DatabaseRepo::class)
    singleOf(::RecipeRepoImpl).bind(RecipeRepo::class)
    singleOf(::NutrientsRepoImpl).bind(NutrientsRepo::class)
}

val storageModule = module {
    singleOf(::InMemoryStorage).bind(Storage::class)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            viewModelModule,
            dataModule,
            dataSourceModule,
            repositoryModule,
            datastoreModule,
            storageModule,
            platformModule()
        )
    }
}
