package com.github.kyhsdjq.tasklist.ui.window;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import com.github.kyhsdjq.tasklist.ui.CLI;

import java.util.Date;
import java.util.List;

public class FilterWindow extends TaskWindow {
    @Override
    public void setTasks(TaskPond taskPond) {
        String askString = "Which attribute would you like to filter by?";
        List<String> options = List.of("date", "tag", "type", "state", "none");
        String answer = CLI.askForString(askString, options);
        switch (answer) {
            case "date":
                filterType = FilterType.DATE;
                filterDate = CLI.askForLocalDate();
                tasks = taskPond.filterByDate(filterDate);
                break;
            case "tag":
                filterType = FilterType.TAG;
                filterTag = CLI.askForString("What's your tag?", null);
                tasks = taskPond.filterByTag(filterTag);
                break;
            case "type":
                filterType = FilterType.TYPE;
                filterClassType = askForType();
                tasks = taskPond.filterByType(filterClassType);
                break;
            case "state":
                filterType = FilterType.STATE;
                filterState = askForState();
                tasks = taskPond.filterByState(filterState);
                break;
            default:
                filterType = FilterType.NONE;
                tasks = taskPond.getTasks();
                break;
        }
        sortTasks();
    }

    private Class<? extends Task> askForType() {
        String askString = "Which type?";
        List<String> options = List.of("onetime", "continuous");
        String answer = CLI.askForString(askString, options);
        if (answer.equals("onetime")) {
            return OnetimeTask.class;
        }
        else {
            return ContinuousTask.class;
        }
    }

    private TaskState askForState() {
        String askString = "Which state?";
        List<String> options = List.of("TODO", "ONGOING", "UNCHECKED", "CHECKED", "COMPLETED");
        String answer = CLI.askForString(askString, options);
        return switch (answer) {
            case "TODO" -> TaskState.TODO;
            case "ONGOING" -> TaskState.ONGOING;
            case "UNCHECKED" -> TaskState.UNCHECKED;
            case "CHECKED" -> TaskState.CHECKED;
            default -> TaskState.COMPLETED;
        };
    }

    @Override
    public void windowLoop(TaskPond taskPond) {
        filterType = FilterType.NONE;
        tasks = taskPond.getTasks();
        boolean breakFlag = false;
        while (!breakFlag) {
            showTasks();
            String askString = "\nChoose an action:";
            List<String> options = List.of("filter", "add", "remove", "edit", "exit");
            String answer = CLI.askForString(askString, options);
            switch (answer) {
                case "filter":
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
        FilterWindow filterWindow = new FilterWindow();
        TaskPond taskPond = new TaskPond();
        filterWindow.windowLoop(taskPond);
    }
}
