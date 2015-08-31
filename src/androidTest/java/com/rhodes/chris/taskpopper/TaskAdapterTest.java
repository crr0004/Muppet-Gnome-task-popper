package com.rhodes.chris.taskpopper;

import com.rhodes.chris.taskpopper.exceptions.TaskAdapterException;

import junit.framework.TestCase;

/**
 * Created by chris on 8/08/15.
 * Test suite for the task adapter class
 */
public class TaskAdapterTest extends TestCase{

    private TaskAdapter testAdapter;

    public void setUp(){
        testAdapter = new TaskAdapter(2);
    }

    public void testLoadStateString(){
        String shouldMatch = "Default 0,Default 1";
        TaskAdapter.RemoveAll();
        testAdapter.loadState(shouldMatch);
        String result = testAdapter.getState();
        assertEquals(shouldMatch, result);
    }

    public void testLoadStateStringZeroLength(){
        String shouldMatch = "";
        TaskAdapter.RemoveAll();
        try{
            testAdapter.loadState(shouldMatch);
            assertTrue("This shouldn't trigger as exception should have been raised", false);
            String result = testAdapter.getState();
            assertEquals(shouldMatch, result);
        }catch (TaskAdapterException e){
            assertEquals("Task adapter loaded with a zero length string", e.getMessage());
        }
    }

    public void testSaveStateString(){
        String shouldMatch = "Default 0,Default 1";
        String result = testAdapter.getState();
        assertEquals(shouldMatch, result);
    }
}
