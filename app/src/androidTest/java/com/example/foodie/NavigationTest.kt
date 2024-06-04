package com.example.foodie

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.foodie.utilities.assertCurrentRouteName
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    // HiltAndroidRule управляет состоянием компонентов и используется для внедрения в ваш тест
    private var hiltRule = HiltAndroidRule(this)
    private val composeTestRule = createComposeRule()

    // Создание цепочки, hiltRule в первую очередь, composeTestRule во вторую
    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(composeTestRule)

    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            Navigation(windowSizeClass = WindowWidthSizeClass.Expanded, navController = navController)
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.foodie", appContext.packageName)
    }

    @Test
    fun navHost_verifyStartDestination() {
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(SPLASH_SCREEN)
    }
//    @Test
//    fun navHost_verifyCatalogScreenDestination(){
//        Thread.sleep(10000)
////        composeTestRule.waitForIdle()
//        navController.assertCurrentRouteName(CATALOG_SCREEN)
//    }
}