# Ratio - A calorie tracker

![ratio-banner](https://github.com/user-attachments/assets/57ae990e-f8e7-4c83-a872-c854573e1d1e)



## Summary
Ratio is a Kotlin Multiplatform Project with shared UI in Compose that targets both iOS and Android devices.

## Description
It's an app that users can use to find out their recommended PFC and calories intake based on several fatcors such as age, weight, activity level, height, intended goal and gender.
It take's into account all these factors to generate their recommended daily PFC, calorie and water intake.
Users can log their create meals, which comprises of various food items that make up that meal.
Tired of having to measure your food before logging?, Ratio allows you to log food for meals just by specifying the size in any context; 'A can of coke', '3 small hard boiled eggs', '3 medium sized cupcakes', while also still being able to specify the size (in grams) if you're feeling picky; '230g of whole wheat bread' and so on.
Users can as well find great recipes their nutritional information as well as share links to your friends or find out more information about them.
'Reports' allows users to log their weights and get a comprehensive graph of their weight journey as well.

## Demo
 ### Android

https://github.com/user-attachments/assets/a82831aa-e36a-456a-a6f9-b89c672836f5


### iOS

https://github.com/user-attachments/assets/b399ff71-baf3-4362-b2e6-b9134eadfbac



## Modules
- commainMain:
  - contains all the shared code between the platforms
- android:
  - contains the android app
- iosMain:
  - contains the ios app


## Data sources
- Edamam Recipe API | [Link](https://developer.edamam.com/)
- Calorie Ninjas API | [Link](https://calorieninjas.com/api)

## Built with
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [DataStore](https://developer.android.com/kotlin/multiplatform/datastore)
- [Koin](https://insert-koin.io/docs/reference/koin-mp/kmp/)
- [ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html)
- [Room](https://developer.android.com/kotlin/multiplatform/room)
- [Coil](https://github.com/coil-kt/coil)
- [Koala Plot](https://koalaplot.github.io/0.5/docs/)
- [Kotlinx.datetime](https://github.com/Kotlin/kotlinx-datetime)
- [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Ktor](https://ktor.io/docs/client-create-multiplatform-application.html)
- [SQLite KMP](https://developer.android.com/kotlin/multiplatform/sqlite)


## 🛠️ Set up 🛠️
 ### API Keys
 1. Get your API keys:
    - [Edamam Developer Portal](https://developer.edamam.com/admin/applications)
    - [CalorieNinjas API Portal](https://calorieninjas.com/api)
    - Create an account and register your application
    - Get your Application ID and Application Key
 2. In the project files navigates to `/Cal/composeApp/src/commonMain/kotlin/com/ghostdev/tracker/cal/ai/data/api`
 3. Insert your Edemam APP_ID and APP_KEY in the `RecipeAPi` file, and your Calorie Ninja API_KEY in the `MealsApi`

   
To run this application you would need;
> **Notice**
> You would need a Mac with macOS installed to write and run iOS-specific code on simulated or real devices.
> This is an Apple requirement.

- Stable internet connection
- Machine running macOS
- [Android Studio](https://developer.android.com/studio) & [Xcode](https://apps.apple.com/us/app/xcode/id497799835) installed
- The Kotlin Multiplatform plugin

### Getting things ready.
- Clone this repo:
  ```shell
    https://github.com/AdrianIkeaba/Ratio_Tracker
  ```

### Android
1. Open the folder in Android Studio, and wait for all dependencies to be installed.
2. Create a new emulator if you haven't already [Create an emulator](https://developer.android.com/studio/run/managing-avds).
3. In your configurations list select `composeApp`.
4. Select your preferred virtual device and click **Run**
5. To run on a physical Android device, enable `Wireless debugging` or `USB debugging`. [More info here](https://developer.android.com/studio/run/device)


  ### iOS
  #### In Android Studio
  1. In the list of configurations select **iosApp** and **Run**, if it doesn't exist, select **Edit Configurations**:
  2. Navigate to **iOS Application** | **iosApp**.
  3. In the **Execution target** list, select your target device and click **OK**:
  4. The `iosApp` run configuration is now available. Click **Run** next to your virtual device:

  #### In Xcode
  1. Click on **Open existing project**
  2. Navigate to the cloned project location, open the folder and select the sub-folder **iosApp**
  3. If you don't have an iOS simulator, [Set up one](https://developer.apple.com/documentation/safari-developer-tools/installing-xcode-and-simulators)
  4. Click on the **▶** button in the top left corner to build and run the the iOS target app.
