package com.rhodes.chris.taskpopper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by chris on 2/08/15.
 */
public class Task {

    private boolean isEnabled = true;
    private String textDesc = "Hello World";
    private boolean isChecked = false;



    public Task(String textDesc){
        this.textDesc = textDesc;
    }

    public boolean isEnabled(){
        return isEnabled;
    }

    public View getViewOfThis(ViewGroup parent, final Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskView = inflater.inflate(R.layout.task_item_view, parent, false);

        taskView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                //TODO This should go in a message to the activity handling this view
                TaskScreenActivity.Handle.sendEmptyMessage(TaskScreenActivity.TASK_LONG_ITEM_CLICK);



                return true;
            }
        });

        ((TextView)taskView.findViewById(R.id.taskItemText)).setText(this.textDesc);


/*
        taskView.findViewById(R.id.taskItemDeleteButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons

                builder.setTitle(context.getString(R.string.task_item_delete_confirmation));

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        TaskAdapter.RemoveTask(Task.this);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
*/
        ((CheckBox)taskView.findViewById(R.id.taskItemDoneCheckBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Task.this.isChecked = isChecked;
                if(isChecked){
                    //TODO This doesn't work. Should gray out task
                 //   Drawable background = ((View)buttonView.getParent()).getBackground();
                   // background.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN);
                }
            }
        });

        return taskView;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public String getDesc() {
        return this.textDesc;
    }
}
