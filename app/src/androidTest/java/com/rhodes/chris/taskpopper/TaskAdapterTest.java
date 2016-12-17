package com.rhodes.chris.taskpopper;

import com.rhodes.chris.taskpopper.exceptions.TaskAdapterException;

import junit.framework.TestCase;

/**
 * Created by chris on 8/08/15.
 * Test suite for the task adapter class
 */
public class TaskAdapterTest extends TestCase{

    private TaskAdapter testAdapter;
    static final int TASK_COUNT = 2;

    public void setUp(){
        testAdapter = new TaskAdapter(TASK_COUNT);
    }

    public void testLoadStateString(){
        String shouldMatch = "Default 0,Default 1,";
        testAdapter.RemoveAll();
        testAdapter.loadState(shouldMatch);
        assertEquals(testAdapter.getCount(), TASK_COUNT);
        assertEquals(((TaskMemento)testAdapter.getTask(0).getMemento()).getTextDesc(), "Default 0");
        assertEquals(((TaskMemento)testAdapter.getTask(1).getMemento()).getTextDesc(), "Default 1");
    }

    public void testLoadStateStringZeroLength(){
        String shouldMatch = "";
        testAdapter.RemoveAll();
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
        String shouldMatch = "Default 0,Default 1,";
        String result = testAdapter.getState();
        assertEquals(shouldMatch, result);
    }
}
