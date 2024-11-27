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
        // TODO: startDate, endDate, nextAlarmTime = currTime

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

    /**
     *
     * @return true if state changed
     */
    public boolean updateState(LocalDate currDate) {
        // TODO: currDate may be redundant
        TaskState prevState = state;
        if (nextAlarmTime.toLocalDate().isAfter(currDate))
            state = TaskState.CHECKED;
        else
            state = TaskState.UNCHECKED;
        return !prevState.equals(state);
    }

    public boolean checkIn() {
        nextAlarmTime = nextAlarmTime.plusDays(1);

        // TODO: Update state
        // updateState(currDate)

        // TODO: Impact on alarm system
        // alarmSystem.updateTask(this);

        return false;
    }

    @Override
    public TaskEditor getTaskEditor() {
        return new ContinuousTaskEditor(this);
    }
}
