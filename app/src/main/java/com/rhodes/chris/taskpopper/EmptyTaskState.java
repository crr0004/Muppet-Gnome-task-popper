package com.rhodes.chris.taskpopper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rhodes.chris.taskpopper.interfaces.iTaskState;

/**
 * Created by chris on 1/9/16.
 * Example of what an empty task list would be
 */
public class EmptyTaskState implements iTaskState {
    @Override
    public boolean isEnabled(TaskContext taskContext) {
        return false;
    }

    @Override
    public View getView(int position, ViewGroup parent, Context context, TaskContext taskContext) {
        return null;
    }

    @Override
    public boolean isActive(TaskContext taskContext) {
        return false;
    }

    @Override
    public void select(TaskContext taskContext) {

    }

    @Override
    public void addTask(TaskContext taskContext) {

    }

    @Override
    public boolean removeTask(TaskContext taskContext, int position) {
        return false;
    }

    @Override
    public void updateTask(TaskContext taskContext) {

    }

    @Override
    public void moveTask(TaskContext taskContext) {

    }

    @Override
    public int getSize(TaskContext taskContext) {
        return 0;
    }

    @Override
    public iTaskState getAt(TaskContext taskContext, int position) {
        return null;
    }

    @Override
    public String getSaveState(TaskContext taskContext) {
        return null;
    }

    @Override
    public int getIDAt(TaskContext taskContext, int position) {
        return 0;
    }

    @Override
    public int getTypeAt(TaskContext taskContext, int position) {
        return 0;
    }
}
