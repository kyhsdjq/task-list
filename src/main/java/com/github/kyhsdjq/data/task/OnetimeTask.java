package com.github.kyhsdjq.data.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OnetimeTask extends Task {
    private LocalDateTime ddlTime;

    private List<LocalDateTime> alarmTimes;

    public OnetimeTask() {
        alarmTimes = new LinkedList<>();
    }

    public void setDdlTime(LocalDateTime ddlTime) {
        this.ddlTime = ddlTime;
    }

    public LocalDateTime getDdlTime() {
        return ddlTime;
    }

    @Override
    public List<LocalDateTime> getAlarmTimes() {
        return new ArrayList<>(alarmTimes);
    }

    private boolean addAlarmTime(LocalDateTime time) {
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
            return true;
        }
        else {
            return false;
        }

        // TODO: Impact on alarm system
        // alarmSystem.remove(this);
    }
}
