package com.rhodes.chris.taskpopper;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rhodes.chris.taskpopper.exceptions.TaskScreenException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TaskScreenActivity extends AppCompatActivity implements Handler.Callback {



    public static final String TASK_SCREEN_ACTIVITY = "TaskScreenActivity";
    public static final int TASK_LONG_ITEM_CLICK = 0;
    public static Handler Handle;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        taskAdapter = new TaskAdapter((ListView)findViewById(R.id.taskListView));


        Intent intent = getIntent();

        if(intent.getAction().equals(getString(R.string.action_add_mass_tasks))){
            if(getIntent().getBooleanExtra(getString(R.string.tasks_saved_key_name), false)) {
                String newTasks = getIntent().getStringExtra(getString(R.string.tasks_saved_list_key_name));
                taskAdapter.loadState(newTasks);
            }
            ((ListView) findViewById(R.id.taskListView)).setAdapter(taskAdapter);
        }else if(savedInstanceState == null) {
            loadFromFile();
        }

        Handle = new Handler(this);

    }

    private void loadFromFile(){
        FileInputStream fis = null;
        try {
            fis = openSavedTasksFileInput();
            int stringLength = readIntFromFile(fis);
            if(stringLength > 0) {
                taskAdapter.loadState(readStringFromFile(fis, stringLength));
                ((ListView)findViewById(R.id.taskListView)).setAdapter(taskAdapter);
            }

        } catch (FileNotFoundException e) {
            Log.v(TASK_SCREEN_ACTIVITY, "Tried to load from persistent memory. File not found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int readIntFromFile(FileInputStream fis) throws IOException {
        ByteBuffer intBuffer = ByteBuffer.allocateDirect(Integer.SIZE /Byte.SIZE);
        int read = fis.read(intBuffer.array());
        if(read < intBuffer.capacity()){
            throw new TaskScreenException("didn't read full int from save file");
        }
        return intBuffer.getInt();
    }

    private String readStringFromFile(FileInputStream fis, int length) throws IOException{
        ByteBuffer stringBuffer = ByteBuffer.allocateDirect(length);
        int read = fis.read(stringBuffer.array());
        if(read < stringBuffer.capacity()){
            throw new TaskScreenException("didn't read full string from save file");
        }
        return new String(stringBuffer.array());
    }

    private void writeIntToFile(FileOutputStream fos, int toWrite) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.SIZE /Byte.SIZE);
        buffer.putInt(toWrite);
        fos.write(buffer.array());
    }

    public boolean addTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setTitle(getString(R.string.task_item_add_dialog_title));

       // @SuppressWarnings()
        View dialogLayoutView = inflater.inflate(R.layout.task_item_add_dialog, null);
        builder.setView(dialogLayoutView);

        final EditText enteredText = (EditText)dialogLayoutView.findViewById(R.id.task_item_add_dialog_task_desc);


        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                TaskAdapter.AddTask(new Task(enteredText.getText().toString()));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });



// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //TODO This doesn't work. Should auto focus on textbox when dialog opens
                enteredText.requestFocus();
            }
        });
        dialog.show();

        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean status = false;
        switch(msg.what){
            case TASK_LONG_ITEM_CLICK:
                status = true;
                ListView listView = ((ListView) findViewById(R.id.taskListView));
                for (int i = 0; i < listView.getChildCount(); i++) {
                    final LinearLayout extraButtons = (LinearLayout) listView.getChildAt(i).findViewById(R.id.taskItemExtraButtonsLayout);
                    ViewGroup.LayoutParams currentLayoutParams = extraButtons.getLayoutParams();
                    currentLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    extraButtons.setLayoutParams(currentLayoutParams);
                    extraButtons.setVisibility(View.VISIBLE);
                }

                break;
        }

        return status;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taskAdapter.loadState(this, savedInstanceState);
        ((ListView)findViewById(R.id.taskListView)).setAdapter(taskAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        taskAdapter.saveState(this, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public FileInputStream openSavedTasksFileInput() throws FileNotFoundException{
        return openFileInput(getString(R.string.tasks_saved_file_name));
    }

    public FileOutputStream openSavedTasksFileOutput() throws FileNotFoundException{
        return openFileOutput(getString(R.string.tasks_saved_file_name), Context.MODE_PRIVATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        FileOutputStream fos = null;
        try {
            fos = openSavedTasksFileOutput();
            String tasksSavedState = taskAdapter.getState();
            writeIntToFile(fos, tasksSavedState.length());
            fos.write(tasksSavedState.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }else if(id == R.id.action_edit){

        }else if(id == R.id.action_add_task) {
            addTask();
        }else if(id == R.id.action_remove){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.abc_are_you_sure);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TaskAdapter.RemoveSelected();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else if(id == R.id.action_add_mass_task){
            startActivity(new Intent(this, MassAddActivity.class));
        }else if(id == R.id.action_clear_all_tasks){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.abc_are_you_sure);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TaskAdapter.RemoveAll();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }


        return super.onOptionsItemSelected(item);
    }
}