package com.github.kyhsdjq.tasklist.data.task;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousTaskTest {

    ContinuousTask task = new ContinuousTask();

    @Test
    void getStartDate() {
        LocalDate time = LocalDate.now().minusDays(10);
        task.setStartDate(time);
        assertEquals(time, task.getStartDate());
    }

    @Test
    void getEndDate() {
        LocalDate time = LocalDate.now().plusDays(10);
        task.setEndDate(time);
        assertEquals(time, task.getEndDate());
    }

    private static void assertAlarmTimes(List<LocalDateTime> alarmTimes, ContinuousTask task) {
        LocalDateTime nextAlarmTime = task.getNextAlarmTime();
        LocalDate endDate = task.getEndDate();
        assertEquals(ChronoUnit.DAYS.between(nextAlarmTime.toLocalDate(), endDate) + 1, alarmTimes.size());
        for (LocalDateTime alarmTime: alarmTimes)
            assertEquals(nextAlarmTime.toLocalTime(), alarmTime.toLocalTime());
        assertEquals(nextAlarmTime.toLocalDate(), alarmTimes.get(0).toLocalDate());
        assertEquals(endDate, alarmTimes.get(alarmTimes.size() - 1).toLocalDate());
    }

    @Test
    void getAlarmTimes() {
        LocalDateTime now = LocalDateTime.now();
        task.setEndDate(now.toLocalDate().plusDays(5));

        // COMPLETED
        task.setNextAlarmTime(now.plusDays(6));
        assertEquals(0, task.getAlarmTimes().size());

        // CHECKED
        task.setNextAlarmTime(now.plusDays(3));
        assertAlarmTimes(task.getAlarmTimes(), task);

        // UNCHECKED
        task.setNextAlarmTime(now.plusMinutes(3));
        assertAlarmTimes(task.getAlarmTimes(), task);
    }

    @Test
    void getState() {
        LocalDateTime now = LocalDateTime.now();
        task.setEndDate(now.toLocalDate().plusDays(5));

        // COMPLETED
        task.setNextAlarmTime(now.plusDays(6));
        assertEquals(TaskState.COMPLETED, task.getState());

        // CHECKED
        task.setNextAlarmTime(now.minusMinutes(1));
        assertEquals(TaskState.CHECKED, task.getState());
        task.setNextAlarmTime(now.plusDays(3));
        assertEquals(TaskState.CHECKED, task.getState());

        // UNCHECKED
        task.setNextAlarmTime(now.plusMinutes(1));
        assertEquals(TaskState.UNCHECKED, task.getState());
    }

    @Test
    void checkIn() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        task.setNextAlarmTime(now);
        task.checkIn();
        assertEquals(now.plusDays(1), task.getNextAlarmTime());
        task.setTaskPond(new TaskPond());
        task.checkIn();
        assertEquals(now.plusDays(2), task.getNextAlarmTime());
    }

    @Test
    void display() {
        task.display();
    }
}
