package com.github.kyhsdjq.tasklist.data;

import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.data.task.TaskState;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

class AlarmSystem {
    private final ScheduledExecutorService scheduler;

    private ScheduledFuture<?> scheduledFuture;

    public record AlarmEntry(LocalDateTime time, Task task) {
        @Override
        public String toString() {
            return time + " - " + task.getName();
        }
    }

    private final List<AlarmEntry> alarmList;

    private final Map<Task, List<AlarmEntry>> alarmMap;

    public AlarmSystem() {
        alarmList = Collections.synchronizedList(new LinkedList<>());
        alarmMap = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduledFuture = null;
    }

    /**
     * @return true if scheduled future is not null
     */
    private boolean resetScheduledFuture () {
        if (scheduledFuture != null && !scheduledFuture.isDone())
            scheduledFuture.cancel(true);
        scheduledFuture = null;

        synchronized (alarmList) {
            if (alarmList.isEmpty()) {
                return false;
            }
            else {
                LocalDateTime now = LocalDateTime.now();
                alarmList.removeIf(new Predicate<AlarmEntry>() {
                    @Override
                    public boolean test(AlarmEntry alarmEntry) {
                        return !alarmEntry.time().isAfter(now);
                    }
                });
                LocalDateTime alarmTime = alarmList.get(0).time();
                long delay = java.time.Duration.between(now, alarmTime).toMillis();
                scheduledFuture = scheduler.schedule(this::alarmEvent, delay, TimeUnit.MILLISECONDS);
                return true;
            }
        }
    }

    private void alarmEvent() {
        System.out.println("\nAlarm! Notice the following tasks: ");
        synchronized (alarmList) {
            int index = 0;
            LocalDateTime now = LocalDateTime.now();
            for (AlarmEntry alarmEntry : alarmList) {
                if (alarmEntry.time().isAfter(now)) {
                    break;
                }
                else {
                    System.out.println("\n --- " + index + " --- ");
                    alarmEntry.task().display();
                    index ++;
                }
            }
        }
        resetScheduledFuture();
    }

    public boolean addTask(Task task) {
        if (alarmMap.containsKey(task)) {
            return false;
        }
        if (task.getState() == TaskState.COMPLETED) {
            return false;
        }
        else if (task.getAlarmTimes().isEmpty()) {
            return false;
        }
        else {
            int index = 0;
            List<AlarmEntry> alarmEntries = new ArrayList<>();
            synchronized (alarmList) {
                for (LocalDateTime alarmTime : task.getAlarmTimes()) {
                    AlarmEntry newAlarmEntry = new AlarmEntry(alarmTime, task);
                    alarmEntries.add(newAlarmEntry);
                    for (; index < alarmList.size(); index++) {
                        if (alarmList.get(index).time().isAfter(alarmTime)) {
                            break;
                        }
                    }
                    alarmList.add(index, newAlarmEntry);
                }
            }
            alarmMap.put(task, alarmEntries);
            resetScheduledFuture();
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
            synchronized (alarmList) {
                alarmList.removeIf(new Predicate<AlarmEntry>() {
                    @Override
                    public boolean test(AlarmEntry alarmEntry) {
                        return alarmEntry.task() == task;
                    }
                });
            }
            alarmMap.remove(task);
            resetScheduledFuture();
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
        if (!alarmMap.containsKey(task))
            return false;

        List<LocalDateTime> prevAlarmTimes = new ArrayList<>();
        for (AlarmEntry alarmEntry: alarmMap.getOrDefault(task, new ArrayList<>())) {
            prevAlarmTimes.add(alarmEntry.time());
        }
        boolean result = !prevAlarmTimes.equals(task.getAlarmTimes());

        if (result) {
            removeTask(task);
            addTask(task);
            resetScheduledFuture();
        }

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
        String result;
        synchronized (alarmList) {
            result = alarmList.toString();
        }
        return result;
    }

    public List<AlarmEntry> getAlarmList() {
        return alarmList;
    }

    public Map<Task, List<AlarmEntry>> getAlarmMap() {
        return alarmMap;
    }
}
