package com.rhodes.chris.taskpopper;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.rhodes.chris.taskpopper.interfaces.iTaskState;

/**
 * Created by chris on 9/1/16.
 * This class is the context of the application state machine
 * This houses the current list of tasks and performs the necessary communication with android & the user
 *
 */
public class TaskContext implements ListAdapter{

    //Each state handles its own transition and is a possible list of lists
    private static iTaskState state;
    public static TaskContext instance;
    private Context androidContext;


    /**
     * Creates an adapter from a saved bundle
     * @see TaskAdapter loadState for format for bundle
     * @param context The android application context
     * @param savedState The bundle with the needed data
     */
    public TaskContext(Context context, Bundle savedState){
        state = new EmptyTaskState();
        instance = this;
        androidContext = context;
    }

    /**
     * Loads the saved tasks from a saved bundle.
     * The bundle must contain @see R.string.tasks_saved_key_name with being true
     * The tasks data is stored in @see R.string.tasks_saved_list_key_name
     * @param context The context of the application
     * @param bundle The bundle in which the data is pulled from
     */
    public void loadState(Context context, Bundle bundle){

    }

    /**
     * Adds the tasks to the task adapter
     * Format of tasks in string: TaskName1,TaskName2,TaskName3
     * That would create three tasks of TaskName1, TaskName2 and TaskName3
     * @param savedTasks String of saved tasks
     */
    public void loadState(String savedTasks) {
        //What happens for a blank string?

    }

    /**
     * Puts the current tasks in this adapter into the provided bundle
     * @param context The context of the application
     * @param bundle The bundle to save to
     */
    public void saveState(Context context, Bundle bundle){

    }

    /**
     * Creates a string of the current tasks in the adapter
     * @return The string containing the saved tasks
     */
    public String getState(){
        return state.getSaveState(this);
    }

    private static void observerChanged(){


    }

    private static void observerInvalidated(){

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

    }

    /**
     * Adds a task to position in the list and refreshes the current listview
     * @param task The task to add
     * @param position The position of the task in the list
     */
    public static void AddTaskAt(Task task, int position){

    }

    public static void RemoveSelected(){

    }

    /**
     * Clears all the tasks from the list and refreshes listview
     */
    public static void RemoveAll(){

    }

    /**
     * Removes a given task from the list and refreshes the current listview
     * @param task The task to remove
     * @return If the removal was successful
     */
    public static boolean RemoveTask(Task task){
        return state.removeTask(instance, 0);
    }

    /**
     * Removes a task at the given position and refreshes the current listview
     * @param position The position of the task to remove
     * @return The removed task
     */
    public static iTaskState RemoveTaskAt(int position){
        iTaskState task = state.getAt(instance, position);
        state.removeTask(instance, position);
        return task;
    }

    @Override
    public boolean areAllItemsEnabled() {

        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return state.isEnabled(this);
    }

    //TODO Check if more than one observer is passed through here
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return state.getSize(this);
    }

    @Override
    public Object getItem(int position) {
        return state.getAt(this, position);
    }

    @Override
    public long getItemId(int position) {
        return state.getIDAt(this, position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return state.getView(position, parent, androidContext, this);
    }

    @Override
    public int getItemViewType(int position) {
        return state.getTypeAt(this, position);
    }

    @Override
    public int getViewTypeCount() {
        return state.getSize(this);
    }

    @Override
    public boolean isEmpty() {
        if(state.getSize(this) > 0){
            return false;
        }
        return true;
    }


}
