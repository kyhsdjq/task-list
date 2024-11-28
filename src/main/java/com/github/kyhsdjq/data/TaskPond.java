package com.github.kyhsdjq.data;

import com.github.kyhsdjq.data.task.Task;
import com.github.kyhsdjq.data.task.TaskState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskPond {
    private final List<Task> tasks;

    AlarmSystem alarmSystem;

    public TaskPond() {
        this(new AlarmSystem());
    }

    public TaskPond(AlarmSystem sys) {
        tasks = new ArrayList<>();
        alarmSystem = sys;
    }

    public boolean addTask(Task task) {
        boolean result = tasks.add(task);
        alarmSystem.addTask(task);
        task.setTaskPond(this);
        return result;
    }

    public boolean removeTask(Task task) {
        boolean result = tasks.remove(task);
        alarmSystem.removeTask(task);
        task.setTaskPond(null);
        return result;
    }

    public List<Task> filterByDate(LocalDate date) {
        List<Task> result = new ArrayList<>();
        for (Task task: tasks) {
            LocalDate startDate = task.getStartDate(), endDate = task.getEndDate();
            if ((startDate == null || startDate.isBefore(date) || startDate.isEqual(date))
                    && endDate.isAfter(date) || endDate.isEqual(date)) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> filterByTag(String tag) {
        List<Task> result = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getTag().equals(tag)) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> filterByType(Class<Task> taskClass) {
        List<Task> result = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getClass() == taskClass) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> filterByState(TaskState taskState) {
        List<Task> result = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getState() == taskState) {
                result.add(task);
            }
        }
        return result;
    }

    public boolean save() {
        // TODO
        return false;
    }

    public boolean load() {
        // TODO
        return false;
    }

    public AlarmSystem getAlarmSystem() {
        return alarmSystem;
    }
}
