package com.rhodes.chris.taskpopper;

/**
 * Created by chris on 23/12/16.
 *
 */

public interface Command {
    void execute();
    void undo();
}
