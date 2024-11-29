package com.github.kyhsdjq.tasklist.ui.window;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import com.github.kyhsdjq.tasklist.ui.CLI;
import com.github.kyhsdjq.tasklist.ui.TaskFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class TaskWindow {
    protected List<Task> tasks;

    // TODO: Refactor to TaskFilter
    enum FilterType{
        DATE, TAG, TYPE, STATE, NONE
    }
    FilterType filterType;
    LocalDate filterDate;
    String filterTag;
    Class<? extends Task> filterClassType;
    TaskState filterState;

    public TaskWindow() {
        tasks = new ArrayList<>();
        filterType = FilterType.NONE;
        filterDate = LocalDate.now();
        filterTag = "default";
        filterClassType = Task.class;
        filterState = TaskState.COMPLETED;
    }

    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks yet.");
        }
        else {
            boolean hasOnetimeTask = false;
            int index = 0;
            for (; index < tasks.size(); index ++) {
                if (tasks.get(index) instanceof OnetimeTask) {
                    if (!hasOnetimeTask)
                        System.out.println("\nOnetime tasks:");
                    hasOnetimeTask = true;
                    System.out.println("\n --- " + index + " --- ");
                    tasks.get(index).display();
                }
                else {
                    break;
                }
            }
            if (!hasOnetimeTask)
                System.out.println("\nNo onetime tasks yet.");

            if (index < tasks.size()) {
                System.out.println("\nContinuous tasks:");
                for (; index < tasks.size(); index ++) {
                    System.out.println("\n --- " + index + " --- ");
                    tasks.get(index).display();
                }
            }
            else {
                System.out.println("\nNo continuous tasks yet.");
            }
        }
    }

    public boolean editTask() {
        if (!tasks.isEmpty()) {
            String askString = "Input index of the task you want to edit.";
            int index;
            while (true) {
                index = CLI.askForInt(askString);
                if (index >= 0 && index < tasks.size())
                    break;
                System.out.println("Please input number between 0 and " + (tasks.size() - 1) + ".");
            }

            return tasks.get(index).getTaskEditor().setTask();
        }
        else {
            System.out.println("There are no tasks for you to edit.");
            return false;
        }
    }

    public boolean addTask(TaskPond taskPond) {
        Task newTask = TaskFactory.getNewTask();
        if(taskPond.addTask(newTask) && filterTask(newTask)) {
            tasks.add(newTask);
            sortTasks();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean filterTask(Task task) {
        return switch (filterType) {
            case DATE -> (task.getStartDate() == null || !task.getStartDate().isAfter(filterDate))
                    && !task.getEndDate().isBefore(filterDate);
            case TAG -> task.getTag().equals(filterTag);
            case TYPE -> task.getClass().equals(filterClassType);
            case STATE -> task.getState().equals(filterState);
            default -> true;
        };
    }

    public boolean removeTask(TaskPond taskPond) {
        if (!tasks.isEmpty()) {
            String askString = "Input index of the task you want to remove.";
            int index;
            while (true) {
                index = CLI.askForInt(askString);
                if (index >= 0 && index < tasks.size())
                    break;
                System.out.println("Please input number between 0 and " + (tasks.size() - 1) + ".");
            }
            if (taskPond.removeTask(tasks.get(index))) {
                tasks.remove(index);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            System.out.println("There are no tasks for you to remove.");
            return false;
        }
    }

    public abstract void setTasks(TaskPond taskPond);

    public abstract void windowLoop(TaskPond taskPond);

    private static class taskComparator implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1 instanceof OnetimeTask) {
                if (o2 instanceof OnetimeTask) {
                    return ((OnetimeTask) o1).getDdlTime().compareTo(((OnetimeTask) o2).getDdlTime());
                }
                else if (o2 instanceof ContinuousTask) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
            else if (o1 instanceof ContinuousTask) {
                if (o2 instanceof OnetimeTask) {
                    return 1;
                }
                else if (o2 instanceof ContinuousTask) {
                    return ((ContinuousTask) o1).getNextAlarmTime().compareTo(((ContinuousTask) o2).getNextAlarmTime());
                }
                else {
                    return 0;
                }
            }
            else {
                return 0;
            }
        }
    }

    protected void sortTasks() {
        tasks.sort(new taskComparator());
    }
}