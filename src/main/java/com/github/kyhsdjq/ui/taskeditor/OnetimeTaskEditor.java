package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.OnetimeTask;

public class OnetimeTaskEditor extends TaskEditor {
    public OnetimeTaskEditor(OnetimeTask ot) {
        task = ot;
    }

    @Override
    public boolean setTask() {
        boolean result = false;

        if (task == null) { // initialize new task
            task = new OnetimeTask();
            result = true;
        }

        // edit old task
        // TODO

        return result;
    }
}
