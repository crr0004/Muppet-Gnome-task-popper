package com.rhodes.chris.taskpopper.com.rhodes.chris.taskpopper.exceptions;

/**
 * Created by chris on 9/08/15.
 * Used to house the exceptions for the application. Provides defaults for exceptions
 */
public class TaskAdapterException extends RuntimeException{
    public TaskAdapterException() {
        super("Task Adapter Exception has been thrown");
    }

    public TaskAdapterException(String detailMessage) {
        super("Task adapter " + detailMessage);
    }
}
