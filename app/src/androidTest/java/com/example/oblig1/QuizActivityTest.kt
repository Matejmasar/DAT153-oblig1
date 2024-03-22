package com.example.oblig1


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click

import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.intent.rule.IntentsTestRule

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class QuizActivityTest {

    @get:Rule
    val quizActivity = IntentsTestRule(QuizActivity::class.java)

    @Test
    fun test_correctAnswer() {
        val rightAnswer = quizActivity.activity.getRightAnswer()
        onView(withText(rightAnswer)).perform(click())
        onView(withId(R.id.scoreNumber)).check(matches(withText("1 / 1")))
    }

    @Test
    fun test_wrongAnswer() {
        val rightAnswer = quizActivity.activity.getRightAnswer()
        val buttonDescriptions = quizActivity.activity.buttonDescriptions()

        for (description in buttonDescriptions) {
            if (description != rightAnswer) {
                onView(withText(description)).perform(click())
                break
            }
        }

        onView(withId(R.id.scoreNumber)).check(matches(withText("0 / 1")))
    }
}