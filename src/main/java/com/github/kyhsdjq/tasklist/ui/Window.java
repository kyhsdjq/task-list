package com.github.kyhsdjq.tasklist.ui;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import com.github.kyhsdjq.tasklist.data.task.Task;

public class Window {
    public static void main(String[] args) {
        TaskPond taskPond = new TaskPond();
        Task task = TaskFactory.getNewTask();
        taskPond.addTask(task);
        taskPond.displayAlarmSystem();
        while (true) {

        }
    }
}
