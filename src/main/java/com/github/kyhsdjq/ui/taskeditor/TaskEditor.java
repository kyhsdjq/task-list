package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.Task;

public abstract class TaskEditor {
    protected Task task;

    public Task getTask() {
        if (task == null)
            setTask();
        return task;
    }

    /**
     * initialize or edit field task with UI
     * @return true if task changed
     */
    public abstract boolean setTask();
}
