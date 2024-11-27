package com.github.kyhsdjq.data.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContinuousTask extends Task {
    private LocalDate startDate, endDate;

    private LocalDateTime nextAlarmTime;

    public ContinuousTask() {}

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
     * @param currDate current date of computer clock
     * @return true if state changed
     */
    public boolean updateState(LocalDate currDate) {
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
}
