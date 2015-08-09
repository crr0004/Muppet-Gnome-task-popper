package com.rhodes.chris.taskpopper;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by chris on 9/08/15.
 * Tests for TaskScreen
 */
public class TaskScreenTest extends ActivityInstrumentationTestCase2<TaskScreen> {

    private TaskScreen activity;
    private TaskAdapter adapter;
    private ListView listView;

    public TaskScreenTest() {
        super(TaskScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
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

}
