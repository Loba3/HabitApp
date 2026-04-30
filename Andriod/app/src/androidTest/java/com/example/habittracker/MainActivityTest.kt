package com.example.habittracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.contrib.RecyclerViewActions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun recyclerViewIsVisible() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun addHabitButtonIsVisible() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAddHabitButton() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addButton))
            .perform(click())
    }
}
