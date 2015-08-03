package com.rhodes.chris.taskpopper;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/08/15.
 * This can only contain one set of items at a time
 * This allows the adding and removing of items from classes that haven't been passed a reference
 */
public class TaskAdapter implements ListAdapter{

    private static List<Task> taskList = new ArrayList<>(1);
    private static DataSetObserver observer;
    private static ListView listHost;
    private static ListAdapter instance;

    //Creates a default list of items for testing
    public TaskAdapter(int numberOfItems){
        for(int i = 0; i < numberOfItems; i++){
            taskList.add(new Task("Default " + i));
        }
        instance = this;

    }

    //Creates an empty adapter that when is added to sets itself to as an adapter
    public TaskAdapter(ListView listHost){
        TaskAdapter.listHost = listHost;
        instance = this;
    }

    //Restores a state
    public TaskAdapter(Context context, Bundle savedState){
        this.loadState(context, savedState);
        instance = this;
    }

    public void loadState(Context context, Bundle bundle){
        if(bundle.getBoolean(context.getString(R.string.tasks_saved_key_name), false)){
            String savedTasks = bundle.getString(context.getString(R.string.tasks_saved_list_key_name), "Default 1");
            loadState(savedTasks);
        }
    }

    public void loadState(String savedTasks) {
        String[] taskDescriptions = savedTasks.split(",");
        for (String taskDesc:
                taskDescriptions) {
            TaskAdapter.AddTask(new Task(taskDesc));
        }
    }

    public void saveState(Context context, Bundle bundle){
        if(getCount() > 0) {
            bundle.putBoolean(context.getString(R.string.tasks_saved_key_name), true);

            bundle.putString(context.getString(R.string.tasks_saved_list_key_name), getState());
        }
    }

    public String getState(){
        StringBuilder tasksString = new StringBuilder();
        tasksString.append(taskList.get(0).getDesc());
        for (int i = 1; i < taskList.size(); i++) {
            tasksString.append(",");
            tasksString.append(taskList.get(i).getDesc());
        }

        return tasksString.toString();
    }

    private static void observerChanged(){
        if(observer != null){
            observer.onChanged();
        }else{
            listHost.setAdapter(TaskAdapter.instance);
        }

    }

    private static void observerInvalidated(){
        if(observer != null){
            observer.onInvalidated();
        }
    }

    /*
    The following methods are static to allow for classes that haven't been passed a reference to the current TaskAdapter to add and remove tasks
     */

    public static void AddTask(Task task){
        TaskAdapter.AddTaskAt(task, taskList.size());
    }

    public static void AddTaskAt(Task task, int position){
        TaskAdapter.taskList.add(position, task);
        if(taskList.size() <= 1){
            observerChanged();
        }

        observerInvalidated();
    }

    public static boolean RemoveTask(Task task){
        boolean status = TaskAdapter.taskList.remove(task);
        observerInvalidated();
        return status;
    }

    public static Task RemoveTaskAt(int position){
        Task task = TaskAdapter.taskList.remove(position);
        observer.onInvalidated();
        return task;
    }

    @Override
    public boolean areAllItemsEnabled() {
        for (Task task: taskList) {
            if(!task.isEnabled()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return taskList.get(position).isEnabled();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        this.observer = observer;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.observer = null;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return taskList.get(position).getViewOfThis(parent, parent.getContext());
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return taskList.size();
    }

    @Override
    public boolean isEmpty() {
        return taskList.isEmpty();
    }


}
