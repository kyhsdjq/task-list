package com.github.kyhsdjq.tasklist.ui;

import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.ui.taskeditor.ContinuousTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.OnetimeTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.TaskEditor;
import java.util.List;

public class TaskFactory {
    public static Task getNewTask() {
        List<String> taskTypes = List.of("onetime", "continuous");
        String taskType = CLI.askForString("Which type of task do you want?", taskTypes);

        TaskEditor taskEditor;
        if (taskType.equals("onetime"))
            taskEditor = new OnetimeTaskEditor(null);
        else
            taskEditor = new ContinuousTaskEditor(null);

        return taskEditor.getTask();
    }
}
