package com.github.kyhsdjq.tasklist.data.task;

import com.github.kyhsdjq.tasklist.ui.taskeditor.ContinuousTaskEditor;
import com.github.kyhsdjq.tasklist.ui.taskeditor.TaskEditor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContinuousTask extends Task {
    private LocalDate startDate, endDate;

    private LocalDateTime nextAlarmTime;

    public ContinuousTask() {
        state = TaskState.UNCHECKED;
        startDate = LocalDate.now();
        endDate = LocalDate.now();
        nextAlarmTime = LocalDateTime.now();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        if (taskPond != null)
            taskPond.sycTask(this);
    }

    @Override
    public List<LocalDateTime> getAlarmTimes() {
        // add specific time of all days in [next alarm time, end date]
        List<LocalDateTime> result = new ArrayList<>();
        if (getState() != TaskState.COMPLETED) {
            LocalDateTime time = getNextAlarmTime();
            while (!time.toLocalDate().isAfter(endDate)) {
                result.add(time);
                time = time.plusDays(1);
            }
        }

        return result;
    }

    public LocalDateTime getNextAlarmTime() {
        updateNextAlarmTime();
        return nextAlarmTime;
    }

    public void setNextAlarmTime(LocalDateTime nextAlarmTime) {
        this.nextAlarmTime = nextAlarmTime;
        if (taskPond != null)
            taskPond.sycTask(this);
    }

    /**
     * @return true if state changed
     */
    private boolean updateState() {
        boolean result = updateNextAlarmTime();
        TaskState prevState = state;
        if (nextAlarmTime.toLocalDate().isAfter(LocalDate.now()))
            state = TaskState.CHECKED;
        else if (nextAlarmTime.toLocalDate().isAfter(endDate))
            state = TaskState.COMPLETED;
        else
            state = TaskState.UNCHECKED;
        return !prevState.equals(state) || result;
    }

    private boolean updateNextAlarmTime() {
        boolean result = false;
        while (!nextAlarmTime.isAfter(LocalDateTime.now())) {
            result = true;
            nextAlarmTime = nextAlarmTime.plusDays(1);
        }
        return result;
    }

    @Override
    public TaskState getState() {
        updateState();
        return super.getState();
    }

    public boolean checkIn() {
        nextAlarmTime = nextAlarmTime.plusDays(1);
        if (taskPond != null)
            taskPond.sycTask(this);
        return true;
    }

    @Override
    public TaskEditor getTaskEditor() {
        return new ContinuousTaskEditor(this);
    }

    @Override
    public void display() {
        displayCommon();
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
    }
}
