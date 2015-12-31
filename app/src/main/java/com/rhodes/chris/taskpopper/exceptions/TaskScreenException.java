package com.rhodes.chris.taskpopper.exceptions;

/**
 * Created by chris on 31/08/15.
 */
public class TaskScreenException extends RuntimeException {

    public TaskScreenException() {
        super("TaskScreen Exception thrown");
    }

    public TaskScreenException(String detailMessage) {
        super("TaskScreen " + detailMessage);
    }
}
