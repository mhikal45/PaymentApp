package com.mmi.paymentapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Test

import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun HomeTabFunction(){
        //Home Tab
        composeTestRule.setContent {
            composeTestRule.onNodeWithText("Sisa Saldo Anda").assertIsDisplayed()
        }
    }
}