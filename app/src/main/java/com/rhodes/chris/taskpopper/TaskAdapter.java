package com.rhodes.chris.taskpopper;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.rhodes.chris.taskpopper.com.rhodes.chris.taskpopper.exceptions.TaskAdapterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/08/15.
 * This can only contain one set of items at a time
 * This allows the adding and removing of items from classes that haven't been passed a reference
 */
public class TaskAdapter implements ListAdapter{

    private static List<Task> taskList = new ArrayList<>(0);
    private static DataSetObserver observer;
    private static ListView listHost;
    private static ListAdapter instance;

    /**
        Creates an adapter with default items
        Items are of text: "Default #n" where #n is the item number
        @param numberOfItems Number of items to initially create
     */
    public TaskAdapter(int numberOfItems){
        taskList.clear();
        for(int i = 0; i < numberOfItems; i++){
            taskList.add(new Task("Default " + i));
        }
        instance = this;

    }

    /**
        Creates an empty adapter. That will attach itself when you add to it for the first time.
        @param listHost The listview which the adapter should attach itself to
     */
    public TaskAdapter(ListView listHost){
        TaskAdapter.listHost = listHost;
        instance = this;
    }

    /**
     * Creates an adapter from a saved bundle
     * @see TaskAdapter loadState for format for bundle
     * @param context The application context
     * @param savedState The bundle with the needed data
     */
    public TaskAdapter(Context context, Bundle savedState){
        this.loadState(context, savedState);
        instance = this;
    }

    /**
     * Loads the saved tasks from a saved bundle.
     * The bundle must contain @see R.string.tasks_saved_key_name with being true
     * The tasks data is stored in @see R.string.tasks_saved_list_key_name
     * @param context The context of the application
     * @param bundle The bundle in which the data is pulled from
     */
    public void loadState(Context context, Bundle bundle){
        //Should this have a test?
        if(bundle.getBoolean(context.getString(R.string.tasks_saved_key_name), false)){
            String savedTasks = bundle.getString(context.getString(R.string.tasks_saved_list_key_name), "Default 1");
            loadState(savedTasks);
        }
    }

    /**
     * Adds the tasks to the task adapter
     * Format of tasks in string: TaskName1,TaskName2,TaskName3
     * That would create three tasks of TaskName1, TaskName2 and TaskName3
     * @param savedTasks String of saved tasks
     */
    public void loadState(String savedTasks) {
        //What happens for a blank string?
        if(savedTasks.length() == 0){
            throw new TaskAdapterException("loaded with a zero length string");
        }
        String[] taskDescriptions = savedTasks.split(",");
        for (String taskDesc:
                taskDescriptions) {
            TaskAdapter.AddTask(new Task(taskDesc));
        }
    }

    /**
     * Puts the current tasks in this adapter into the provided bundle
     * @param context The context of the application
     * @param bundle The bundle to save to
     */
    public void saveState(Context context, Bundle bundle){
        if(getCount() > 0) {
            bundle.putBoolean(context.getString(R.string.tasks_saved_key_name), true);

            bundle.putString(context.getString(R.string.tasks_saved_list_key_name), getState());
        }
    }

    /**
     * Creates a string of the current tasks in the adapter
     * @return The string containing the saved tasks
     */
    public String getState(){
        StringBuilder tasksString = new StringBuilder();
        if(taskList.size() > 0) {
            tasksString.append(taskList.get(0).getDesc());
            for (int i = 1; i < taskList.size(); i++) {
                tasksString.append(",");
                tasksString.append(taskList.get(i).getDesc());
            }
        }

        return tasksString.toString();
    }

    private static void observerChanged(){
        if(observer != null){
            observer.onChanged();
        }else{
            //Should this have an error message saying the listHost is null?
            if(listHost != null){
                listHost.setAdapter(TaskAdapter.instance);
            }

        }

    }

    private static void observerInvalidated(){
        if(observer != null){
            observer.onInvalidated();
        }
    }

    /*
    The following methods are static to prevent 'reference hell'.
    This allows other classes to access TaskAdapter to add and remove tasks without a reference
     */

    /**
     * Adds a task to the end of the list and refreshes the current listview
     * @param task The task to add
     */
    public static void AddTask(Task task){
        TaskAdapter.AddTaskAt(task, taskList.size());
    }

    /**
     * Adds a task to position in the list and refreshes the current listview
     * @param task The task to add
     * @param position The position of the task in the list
     */
    public static void AddTaskAt(Task task, int position){
        TaskAdapter.taskList.add(position, task);
        if(taskList.size() <= 1){
            observerChanged();
        }

        observerInvalidated();
    }

    /**
     * Clears all the tasks from the list and refreshes listview
     */
    public static void RemoveAll(){
        taskList.clear();
        observerChanged();
    }

    /**
     * Removes a given task from the list and refreshes the current listview
     * @param task The task to remove
     * @return If the removal was successful
     */
    public static boolean RemoveTask(Task task){
        boolean status = TaskAdapter.taskList.remove(task);
        observerInvalidated();
        return status;
    }

    /**
     * Removes a task at the given position and refreshes the current listview
     * @param position The position of the task to remove
     * @return The removed task
     */
    public static Task RemoveTaskAt(int position){
        Task task = TaskAdapter.taskList.remove(position);
        observer.onInvalidated();
        return task;
    }

    /**
     * Transforms text with newlines to the format where this task adapter can load the tasks from a string
     * @param tasks Text with tasks separated by a newline
     * @return string which can be used in loadState to create tasks
     */
    public static String TransformNewLineString(String tasks){

        if(tasks.length() == 0){
            throw new TaskAdapterException("Can't transform new line string with zero length");
        }

        String[] separatedTasks = tasks.split("\n");
        StringBuilder newTasksBuffer = new StringBuilder();

        newTasksBuffer.append(separatedTasks[0]);
        for(int i = 1; i < separatedTasks.length; i++){
            newTasksBuffer.append(",");
            newTasksBuffer.append(separatedTasks[i]);
        }

        return newTasksBuffer.toString();
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

    //TODO Check if more than one observer is passed through here
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        TaskAdapter.observer = observer;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        TaskAdapter.observer = null;
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
