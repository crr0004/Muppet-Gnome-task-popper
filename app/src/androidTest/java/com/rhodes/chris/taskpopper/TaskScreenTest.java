package com.rhodes.chris.taskpopper;

import android.test.suitebuilder.annotation.LargeTest;
import android.widget.ListView;

import org.junit.*;
import org.junit.runner.RunWith;

import android.support.test.rule.*;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by chris on 9/08/15.
 * Tests for TaskScreenActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskScreenTest {

    private TaskScreenActivity activity;
    private TaskAdapter adapter;
    private ListView listView;

    @Rule
    public ActivityTestRule<TaskScreenActivity> mActivityRule = new ActivityTestRule<>(
            TaskScreenActivity.class);

    @Test
    public void pressAddButtonTest(){
        onView(withId(R.id.action_add_task)).perform(click());
        onView(withId(R.id.task_item_add_dialog_layout)).check(matches(isDisplayed()));
    }

/*
    public TaskScreenTest() {
        super(TaskScreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        activity = getActivity();
        adapter = new TaskAdapter(2);
        listView = (ListView)activity.findViewById(R.id.taskListView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        activity = null;
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        assertNotNull("listView is null", listView);
    }
    */

}
