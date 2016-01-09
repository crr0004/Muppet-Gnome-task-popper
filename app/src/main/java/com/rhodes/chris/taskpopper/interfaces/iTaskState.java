package com.rhodes.chris.taskpopper.interfaces;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rhodes.chris.taskpopper.TaskContext;

/**
 * Created by chris on 1/9/16.
 * The abstract class for task state
 * All tasks must implement this and the context will support all methods being 'passed up'
 */
public interface iTaskState {

    boolean isEnabled(TaskContext taskContext);
    View getView(int position, ViewGroup parent, final Context context, TaskContext taskContext);
    boolean isActive(TaskContext taskContext);
    void select(TaskContext taskContext);
    void addTask(TaskContext taskContext);
    boolean removeTask(TaskContext taskContext, int position);
    void updateTask(TaskContext taskContext);
    void moveTask(TaskContext taskContext);
    int getSize(TaskContext taskContext);
    iTaskState getAt(TaskContext taskContext, int position);
    String getSaveState(TaskContext taskContext);
    int getIDAt(TaskContext taskContext, int position);
    int getTypeAt(TaskContext taskContext, int position);
}
