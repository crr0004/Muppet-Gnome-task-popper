package com.rhodes.chris.taskpopper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TaskScreen extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    //TODO Move to method
    private MenuItem.OnMenuItemClickListener addTaskMenuAction = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            AlertDialog.Builder builder = new AlertDialog.Builder(TaskScreen.this);
// Add the buttons

            LayoutInflater inflater = TaskScreen.this.getLayoutInflater();

            builder.setTitle(getString(R.string.task_item_add_dialog_title));

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        taskAdapter = new TaskAdapter((ListView)findViewById(R.id.taskListView));

        FileInputStream fis = null;
        if(savedInstanceState == null) {
            try {
                fis = openFileInput(getString(R.string.tasks_saved_file_name));
                int stringLength = readIntFromFile(fis);
                taskAdapter.loadState(readStringFromFile(fis, stringLength));

            } catch (FileNotFoundException e) {
                //TODO Tag should be const in class
                Log.v("TaskScreen", "Tried to load from persistent memory. File not found");
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

    }

    private int readIntFromFile(FileInputStream fis) throws IOException {
        ByteBuffer intBuffer = ByteBuffer.allocateDirect(Integer.SIZE /Byte.SIZE);
        fis.read(intBuffer.array());
        return intBuffer.getInt();
    }

    private String readStringFromFile(FileInputStream fis, int length) throws IOException{
        ByteBuffer stringBuffer = ByteBuffer.allocateDirect(length);
        fis.read(stringBuffer.array());
        return new String(stringBuffer.array());
    }

    private void writeIntToFile(FileOutputStream fos, int toWrite) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.SIZE /Byte.SIZE);
        buffer.putInt(toWrite);
        fos.write(buffer.array());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
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

    @Override
    protected void onStop() {
        super.onStop();
        FileOutputStream fos = null;
//TODO This should have a test
        try {
            fos = openFileOutput(getString(R.string.tasks_saved_file_name), Context.MODE_PRIVATE);
            String tasksSavedState = taskAdapter.getState();
            writeIntToFile(fos, tasksSavedState.length());
            fos.write(tasksSavedState.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        //TODO This shouldn't be here. Should be in onOptionsItemSelected function and the method called from there
        menu.findItem(R.id.action_add_task).setOnMenuItemClickListener(addTaskMenuAction);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
