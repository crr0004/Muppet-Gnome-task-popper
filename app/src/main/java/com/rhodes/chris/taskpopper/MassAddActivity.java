package com.rhodes.chris.taskpopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MassAddActivity extends AppCompatActivity {


    private EditText userText;
    private Intent goToTaskScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massadd);

        userText = (EditText)findViewById(R.id.newMassTasksTextView);
        goToTaskScreen = new Intent(this, TaskScreenActivity.class);
        goToTaskScreen.setAction(getString(R.string.action_add_mass_tasks));

        findViewById(R.id.massTasksDoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText = userText.getText().toString();

                if(editText.length() > 0) {
                    goToTaskScreen.putExtra(getString(R.string.tasks_saved_key_name), true);
                    goToTaskScreen.putExtra(getString(R.string.tasks_saved_list_key_name),
                            TaskAdapter.TransformNewLineString(editText));
                }
                startActivity(goToTaskScreen);
            }
        });

        findViewById(R.id.massTasksCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToTaskScreen);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_massadd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_text) {

            userText.setText("");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
