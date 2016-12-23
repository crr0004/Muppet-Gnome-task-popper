package com.rhodes.chris.taskpopper;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.rhodes.chris.taskpopper.exceptions.TaskAdapterException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by chris on 8/08/15.
 * Test suite for the task adapter class
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskAdapterTest {

    private TaskAdapter testAdapter;
    private static final int TASK_COUNT = 2;

    @Before
    public void setUp(){
        testAdapter = new TaskAdapter(TASK_COUNT);
    }

    @Test
    public void AddSimpleTaskCommandTest(){
        TaskAdapter adapter = new TaskAdapter();
        String taskDesc = "Default 0";
        Command addTaskCommand = new AddSimpleTaskCommand(adapter, taskDesc);
        for(int i = 0; i < 10; i++) {
            addTaskCommand.execute();
            assertEquals(adapter.getCount(), 1);
            assertEquals(adapter.getTask(0).getDesc(), taskDesc);
            addTaskCommand.undo();
            assertEquals(adapter.getCount(), 0);
        }
    }

    @Test
    public void ReplaceSimpleTaskCommandTest(){
        TaskAdapter adapter = new TaskAdapter();
        String taskDesc = "Default 0";
        String replaceTaskDesc = "Default 1";
        adapter.AddTask(new Task(taskDesc));
        assertEquals(adapter.getTask(0).getDesc(), taskDesc);

        Command replaceTaskCommand = new ReplaceSimpleTaskCommand(adapter, replaceTaskDesc, 0);

        replaceTaskCommand.execute();
        assertEquals(adapter.getTask(0).getDesc(), replaceTaskDesc);
        replaceTaskCommand.undo();
        assertEquals(adapter.getTask(0).getDesc(), taskDesc);
    }

    @Test
    public void testLoadStateString(){
        String shouldMatch = "Default 0,Default 1,";
        testAdapter.RemoveAll();
        testAdapter.loadState(shouldMatch);
        assertEquals(testAdapter.getCount(), TASK_COUNT);
        assertEquals(((TaskMemento)testAdapter.getTask(0).getMemento()).getTextDesc(), "Default 0");
        assertEquals(((TaskMemento)testAdapter.getTask(1).getMemento()).getTextDesc(), "Default 1");
    }

    @Test
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

    @Test
    public void testSaveStateString(){
        String shouldMatch = "Default 0,Default 1,";
        String result = testAdapter.getState();
        assertEquals(shouldMatch, result);
    }
}
