package com.github.kyhsdjq.ui;

import com.github.kyhsdjq.data.task.Task;
import com.github.kyhsdjq.ui.taskeditor.ContinuousTaskEditor;
import com.github.kyhsdjq.ui.taskeditor.OnetimeTaskEditor;
import com.github.kyhsdjq.ui.taskeditor.TaskEditor;
import java.util.List;

public class TaskFactory {
    public static Task getNewTask() {
        List<String> taskTypes = List.of("onetime", "continuous");
        String taskType = CLI.askForString("Which type of task do you want?", taskTypes);

        TaskEditor taskEditor = null;
        if (taskType.equals("onetime"))
            taskEditor = new OnetimeTaskEditor(null);
        else
            taskEditor = new ContinuousTaskEditor(null);

        return taskEditor.getTask();
    }
}
