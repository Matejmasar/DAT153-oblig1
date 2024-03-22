package com.example.oblig1

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val mainActivity = IntentsTestRule(MainActivity::class.java)

    @Test
    fun test_quizButtonClicked() {
        onView(withId(R.id.quiz_button)).perform(click())

        intended(hasComponent(QuizActivity::class.java.name))
    }

    @Test
    fun test_galleryButtonClicked() {
        onView(withId(R.id.gallery_button)).perform(click())

        intended(hasComponent(GalleryActivity::class.java.name))
    }
}