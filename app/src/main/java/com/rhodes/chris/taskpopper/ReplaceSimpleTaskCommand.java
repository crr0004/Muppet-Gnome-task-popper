package com.rhodes.chris.taskpopper;

/**
 * Created by chris on 23/12/16.
 *
 */

public class ReplaceSimpleTaskCommand implements Command {

    private TaskAdapter adapter;
    private String desc;
    private int indexToReplace;
    private Task replacingTask;

    public ReplaceSimpleTaskCommand(TaskAdapter adapter, String taskDesc, int indexToReplace){
        this.adapter = adapter;
        this.desc = taskDesc;
        this.indexToReplace = indexToReplace;
    }

    @Override
    public void execute() {
        replacingTask = adapter.getTask(indexToReplace);
        adapter.RemoveTaskAt(indexToReplace);
        adapter.AddTaskAt(new Task(desc), indexToReplace);
    }

    @Override
    public void undo() {
        adapter.RemoveTaskAt(indexToReplace);
        adapter.AddTaskAt(replacingTask, indexToReplace);
    }
}
