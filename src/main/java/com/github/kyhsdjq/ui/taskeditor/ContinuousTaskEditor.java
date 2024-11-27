package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.ContinuousTask;

public class ContinuousTaskEditor extends TaskEditor {
    public ContinuousTaskEditor(ContinuousTask ct) {
        task = ct;
    }

    @Override
    public boolean setTask() {
        boolean result = false;

        if (task == null) { // initialize new task
            task = new ContinuousTask();
            result = true;
        }

        // edit old task
        // TODO

        return result;
    }
}
