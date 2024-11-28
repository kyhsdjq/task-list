package com.github.kyhsdjq.data.task;

import com.github.kyhsdjq.ui.taskeditor.ContinuousTaskEditor;
import com.github.kyhsdjq.ui.taskeditor.OnetimeTaskEditor;
import com.github.kyhsdjq.ui.taskeditor.TaskEditor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    }

    @Override
    public List<LocalDateTime> getAlarmTimes() {
        return new ArrayList<>(Collections.singleton(nextAlarmTime));
    }

    public LocalDateTime getNextAlarmTime() {
        return nextAlarmTime;
    }

    public void setNextAlarmTime(LocalDateTime nextAlarmTime) {
        this.nextAlarmTime = nextAlarmTime;
    }

    /**
     * @return true if state changed
     */
    private boolean updateState() {
        TaskState prevState = state;
        if (nextAlarmTime.toLocalDate().isAfter(LocalDate.now()))
            state = TaskState.CHECKED;
        else if (nextAlarmTime.toLocalDate().isAfter(endDate))
            state = TaskState.COMPLETED;
        else
            state = TaskState.UNCHECKED;
        return !prevState.equals(state);
    }

    @Override
    public TaskState getState() {
        updateState();
        return super.getState();
    }

    public boolean checkIn() {
        nextAlarmTime = nextAlarmTime.plusDays(1);
        return false;
    }

    @Override
    public TaskEditor getTaskEditor() {
        return new ContinuousTaskEditor(this);
    }
}
