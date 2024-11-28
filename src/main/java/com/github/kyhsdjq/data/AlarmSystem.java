package com.github.kyhsdjq.data;

import com.github.kyhsdjq.data.task.Task;
import com.github.kyhsdjq.data.task.TaskState;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class AlarmSystem {
    private record AlarmEntry(LocalDateTime time, Task task) {
        @Override
        public String toString() {
            return time + " - " + task.getName();
        }
    }

    private final List<AlarmEntry> alarmList;

    private final Map<Task, List<AlarmEntry>> alarmMap;

    public AlarmSystem() {
        alarmList = new LinkedList<>();
        alarmMap = new HashMap<>();
    }

    public boolean addTask(Task task) {
        if (task.getState() == TaskState.COMPLETED) {
            return false;
        }
        else if (task.getAlarmTimes().isEmpty()) {
            return false;
        }
        else {
            int index = 0;
            List<AlarmEntry> alarmEntries = new ArrayList<>();
            for (LocalDateTime alarmTime: task.getAlarmTimes()) {
                AlarmEntry newAlarmEntry = new AlarmEntry(alarmTime, task);
                alarmEntries.add(newAlarmEntry);
                for (; index < alarmList.size(); index ++) {
                    if (alarmList.get(index).time().isAfter(alarmTime)) {
                        break;
                    }
                }
                alarmList.add(index, newAlarmEntry);
            }
            alarmMap.put(task, alarmEntries);
            return true;
        }
    }

    public boolean addAllTasks(Collection<Task> tasks) {
        boolean result = false;
        for (Task task: tasks) {
            result = addTask(task) || result;
        }
        return result;
    }

    public boolean removeTask(Task task) {
        List<AlarmEntry> alarmEntries = alarmMap.getOrDefault(task, null);
        if (alarmEntries == null) {
            return false;
        }
        else {
            int index = 0;
            alarmList.removeIf(new Predicate<AlarmEntry>() {
                @Override
                public boolean test(AlarmEntry alarmEntry) {
                    return alarmEntry.task() == task;
                }
            });
            alarmMap.remove(task);

            return true;
        }
    }

    public boolean removeAllTasks(Collection<Task> tasks) {
        boolean result = false;
        for (Task task: tasks) {
            result = removeTask(task) || result;
        }
        return result;
    }

    public boolean updateTask(Task task) {
        List<LocalDateTime> prevAlarmTimes = new ArrayList<>();
        for (AlarmEntry alarmEntry: alarmMap.getOrDefault(task, new ArrayList<>())) {
            prevAlarmTimes.add(alarmEntry.time());
        }
        boolean result = prevAlarmTimes.equals(task.getAlarmTimes());

        removeTask(task);
        addTask(task);
        return result;
    }

    public boolean updateAllTasks(Collection<Task> tasks) {
        boolean result = false;
        for (Task task: tasks) {
            result = updateTask(task) || result;
        }
        return result;
    }


    @Override
    public String toString() {
        return alarmList.toString();
    }
}
