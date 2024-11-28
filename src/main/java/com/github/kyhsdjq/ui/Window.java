package com.github.kyhsdjq.ui;

import com.github.kyhsdjq.data.TaskPond;
import com.github.kyhsdjq.data.task.Task;

public class Window {
    public static void main(String[] args) {
        TaskPond taskPond = new TaskPond();
        Task task = TaskFactory.getNewTask();
        taskPond.addTask(task);
        System.out.println(taskPond.getAlarmSystem());
        task.getTaskEditor().setTask();
        System.out.println(taskPond.getAlarmSystem());
    }
}
