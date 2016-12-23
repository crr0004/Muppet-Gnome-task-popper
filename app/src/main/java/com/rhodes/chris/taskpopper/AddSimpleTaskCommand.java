package com.rhodes.chris.taskpopper;

/**
 * Created by chris on 23/12/16.
 *
 */

public class AddSimpleTaskCommand implements Command {

    private TaskAdapter adapter;
    private String desc;
    private int index;



    public AddSimpleTaskCommand(TaskAdapter adapter, String taskDesc){
        this.adapter = adapter;
        this.desc = taskDesc;
    }

    @Override
    public void execute() {
        index = adapter.addTask(new Task(desc));
    }

    @Override
    public void undo() {
        adapter.RemoveTaskAt(index);
    }
}
