package com.github.kyhsdjq.tasklist.ui.window;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import com.github.kyhsdjq.tasklist.ui.CLI;
import com.github.kyhsdjq.tasklist.ui.TaskFactory;

import java.time.LocalDate;
import java.util.List;

public class TimelineWindow extends TaskWindow {
    @Override
    public void setTasks(TaskPond taskPond) {
        filterType = FilterType.DATE;
        filterDate = CLI.askForLocalDate();
        tasks = taskPond.filterByDate(filterDate);
        sortTasks();
    }

    @Override
    public void windowLoop(TaskPond taskPond) {
        filterType = FilterType.DATE;
        filterDate = LocalDate.now();
        tasks = taskPond.filterByDate(filterDate);
        boolean breakFlag = false;
        while (!breakFlag) {
            System.out.println("\nDate: " + filterDate);
            showTasks();
            String askString = "\nChoose an action:";
            List<String> options = List.of("date", "add", "remove", "edit", "exit");
            String answer = CLI.askForString(askString, options);
            switch (answer) {
                case "date":
                    setTasks(taskPond);
                    break;
                case "add":
                    if (!addTask(taskPond))
                        System.out.println("Fail to add task.");
                    break;
                case "remove":
                    if (!removeTask(taskPond))
                        System.out.println("Fail to remove task.");
                    break;
                case "edit":
                    if (!editTask())
                        System.out.println("Fail to edit task.");
                    break;
                default:
                    breakFlag = true;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        TimelineWindow timelineWindow = new TimelineWindow();
        TaskPond taskPond = new TaskPond();
        timelineWindow.windowLoop(taskPond);
    }
}
