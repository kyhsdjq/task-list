package com.github.kyhsdjq.tasklist.ui;

import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.ui.taskeditor.ContinuousTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.OnetimeTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.TaskEditor;
import java.util.List;
import java.util.Scanner;

public class TaskFactory {
    public static Task getNewTask() {
        return getNewTask(new Scanner(System.in));
    }

    public static Task getNewTask(Scanner scanner) {
        List<String> taskTypes = List.of("onetime", "continuous");
        String taskType = CLI.askForString("Which type of task do you want?", taskTypes, scanner);

        TaskEditor taskEditor;
        if (taskType.equals("onetime"))
            taskEditor = new OnetimeTaskEditor(null);
        else
            taskEditor = new ContinuousTaskEditor(null);

        taskEditor.setScanner(scanner);

        return taskEditor.getTask();
    }
}
