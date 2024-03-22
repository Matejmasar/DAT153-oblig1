package com.example.oblig1




import android.net.Uri

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended

import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GalleryActivityTest {

    @get:Rule
    val galleryActivity = IntentsTestRule(GalleryActivity::class.java)

    @Test
    fun test_addPhoto() {
        val view = galleryActivity.activity.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val numberOfItems = view.adapter?.itemCount ?: 0

        addNewPhoto()

        // check if the number of items in the recycler view has increased
        val newNumberOfItems = view.adapter?.itemCount ?: 0
        assert(newNumberOfItems == numberOfItems + 1)

    }

    @Test
    fun test_removePhoto(){
        val view = galleryActivity.activity.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val numberOfItems = view.adapter?.itemCount ?: 0

        onView(withId(R.id.galleryRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withText(R.string.yes)).perform(click())

        // check if the number of items in the recycler view has decreased
        val newNumberOfItems = view.adapter?.itemCount ?: 0
        assert(newNumberOfItems == numberOfItems - 1)

    }

    private fun addNewPhoto() {
        // open photo selector activity
        onView(withId(R.id.addPhotoButton)).perform(click())
        intended(hasComponent(PhotoSelectorActivity::class.java.name))

        // mock photo selection
        mockPhotoSelection()
    }

    private fun mockPhotoSelection(){
        // Create an expected result Uri
        val expectedResultUri = Uri.parse("android.resource://com.example.oblig1/${R.drawable.dog}")

        // Create a custom ActivityResultRegistry
        val testRegistry = object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                // Dispatch the predefined result URI when the OpenDocument contract is launched
                if (contract is ActivityResultContracts.OpenDocument) {
                    dispatchResult(requestCode, expectedResultUri)
                }
            }
        }

        // Launch the PhotoSelectorActivity with the custom registry
        with(ActivityScenario.launch(PhotoSelectorActivity(testRegistry)::class.java)) {
            onActivity {
                onView(withId(R.id.selectPhotoDescription)).perform(typeText("Test image"), closeSoftKeyboard())
                onView(withId(R.id.selectPhotoButton)).perform(click())
                onView(withId(R.id.selectPhotoSave)).perform(click())
            }
        }

    }



}