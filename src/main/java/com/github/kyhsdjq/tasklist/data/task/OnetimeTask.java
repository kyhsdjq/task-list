package com.github.kyhsdjq.tasklist.data.task;

import com.github.kyhsdjq.tasklist.ui.taskeditor.OnetimeTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.TaskEditor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class OnetimeTask extends Task {
    private LocalDateTime ddlTime;

    private List<LocalDateTime> alarmTimes;

    public OnetimeTask() {
        state = TaskState.TODO;
        ddlTime = LocalDateTime.now();
        alarmTimes = new LinkedList<>();
    }

    public void setDdlTime(LocalDateTime ddlTime) {
        this.ddlTime = ddlTime;
    }

    public LocalDateTime getDdlTime() {
        return ddlTime;
    }

    @Override
    public LocalDate getStartDate() {
        return null;
    }

    @Override
    public LocalDate getEndDate() {
        return ddlTime.toLocalDate();
    }

    @Override
    public List<LocalDateTime> getAlarmTimes() {
        if (getState() == TaskState.COMPLETED) {
            alarmTimes.clear();
        }
        else {
            alarmTimes.removeIf(new Predicate<LocalDateTime>() {
                @Override
                public boolean test(LocalDateTime localDateTime) {
                    return !localDateTime.isAfter(LocalDateTime.now());
                }
            });
        }
        return new ArrayList<>(alarmTimes);
    }

    public boolean addAlarmTime(LocalDateTime time) {
        int i = 0;
        for (; i < alarmTimes.size(); i ++) {
            if (alarmTimes.get(i).isEqual(time)) {
                return false;
            }
            else if (alarmTimes.get(i).isAfter(time)) {
                break;
            }
        }
        alarmTimes.add(i, time);
        if (taskPond != null)
            taskPond.sycTask(this);
        return true;
    }

    public boolean addAlarmTime(Duration duration) {
        LocalDateTime newAlarmTime = ddlTime.minus(duration);
        return addAlarmTime(newAlarmTime);
    }

    public boolean removeAlarmTime(LocalDateTime time) {
        int i = 0;
        for (; i < alarmTimes.size(); i ++) {
            if (alarmTimes.get(i).isEqual(time)) {
                alarmTimes.remove(i);
                if (taskPond != null)
                    taskPond.sycTask(this);
                return true;
            }
            else if (alarmTimes.get(i).isAfter(time)) {
                return false;
            }
        }
        return false;
    }

    public boolean removeAlarmTime(Duration duration) {
        LocalDateTime newAlarmTime = ddlTime.minus(duration);
        return removeAlarmTime(newAlarmTime);
    }

    public boolean start() {
        if (state == TaskState.TODO) {
            state = TaskState.ONGOING;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean complete() {
        if (state == TaskState.ONGOING) {
            state = TaskState.COMPLETED;
            if (taskPond != null)
                taskPond.sycTask(this);
            return true;
        }
        else {
            return false;
        }
    }

    public void setState(TaskState taskState) {
        this.state = taskState;
        if (taskPond != null)
            taskPond.sycTask(this);
    }

    @Override
    public TaskEditor getTaskEditor() {
        return new OnetimeTaskEditor(this);
    }

    @Override
    public void display() {
        displayCommon();
        System.out.println("ddlTime: " + ddlTime);
    }
}
