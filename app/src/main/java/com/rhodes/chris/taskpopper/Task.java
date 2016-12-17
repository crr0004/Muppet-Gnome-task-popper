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
 *
 */
class Task {

    private boolean isEnabled = true;
    private String textDesc = "Hello World";
    private boolean isChecked = false;
    private View viewOfThis;
    private boolean isExpanded = false;



    public Task(String textDesc){
        this.textDesc = textDesc;
    }

       public boolean isEnabled(){
        return isEnabled;
    }

    public View getViewOfThis(ViewGroup parent, final Context context){
        if(isExpanded || viewOfThis == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View taskView = inflater.inflate(R.layout.task_item_view, parent, false);


            taskView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TaskScreenActivity.Handle.sendEmptyMessage(TaskScreenActivity.TASK_LONG_ITEM_CLICK);
                    return true;
                }
            });

            ((TextView) taskView.findViewById(R.id.taskItemText)).setText(this.textDesc);

            ((CheckBox) taskView.findViewById(R.id.taskItemDoneCheckBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Task.this.isChecked = isChecked;
                    if (isChecked) {
                        //TODO This doesn't work. Should gray out task
                        //   Drawable background = ((View)buttonView.getParent()).getBackground();
                        // background.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN);
                    }
                    buttonView.invalidate();
                }
            });
            viewOfThis = taskView;
        }
        isExpanded = false;

        return viewOfThis;
    }

    public View getExpandedView(ViewGroup parent, final Context context){
        if(!isExpanded) {
            View view = getViewOfThis(parent, context);
            isExpanded = true;
            final LinearLayout extraButtons = (LinearLayout) view.findViewById(R.id.taskItemExtraButtonsLayout);
            ViewGroup.LayoutParams currentLayoutParams = extraButtons.getLayoutParams();
            currentLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            extraButtons.setLayoutParams(currentLayoutParams);
            extraButtons.setVisibility(View.VISIBLE);
        }
        return  viewOfThis;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public String getDesc() {
        return this.textDesc;
    }

    public Memento getMemento(){
        TaskMemento memento = new TaskMemento();
        memento.setEnabled(isEnabled);
        memento.setTextDesc(textDesc);
        return memento;
    }

    public void setMemento(Memento memento){
        TaskMemento state = (TaskMemento)memento;
        this.isEnabled = state.isEnabled();
        this.textDesc = state.getTextDesc();

    }


}

class TaskMemento implements Memento{
    private boolean isEnabled;
    private String textDesc;

    boolean isEnabled() {
        return isEnabled;
    }

    void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    String getTextDesc() {
        return textDesc;
    }

    void setTextDesc(String textDesc) {
        this.textDesc = textDesc;
    }
}

