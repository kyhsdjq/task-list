package com.github.kyhsdjq.tasklist.data.task;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.github.kyhsdjq.tasklist.data.TaskPond;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OnetimeTaskTest {

    private final OnetimeTask task = new OnetimeTask();

    private static boolean checkOrderliness(List<LocalDateTime> times) {
        for (int i = 1; i < times.size(); i++) {
            if (!times.get(i).isAfter(times.get(i - 1))) {
                return false;
            }
        }
        return true;
    }

    @Test
    void getAlarmTimes() {
        LocalDateTime now = LocalDateTime.now();

        task.setDdlTime(now.plusDays(1));
        task.setState(TaskState.TODO);

        // test orderliness
        task.addAlarmTime(Duration.ofMinutes(4)); assert(checkOrderliness(task.getAlarmTimes()));
        task.addAlarmTime(Duration.ofMinutes(1)); assert(checkOrderliness(task.getAlarmTimes()));
        task.addAlarmTime(Duration.ofMinutes(3)); assert(checkOrderliness(task.getAlarmTimes()));
        task.addAlarmTime(Duration.ofMinutes(5)); assert(checkOrderliness(task.getAlarmTimes()));
        task.addAlarmTime(Duration.ofMinutes(2)); assert(checkOrderliness(task.getAlarmTimes()));

        // test output
        assertEquals(5, task.getAlarmTimes().size());
        for (int i = 0; i < 5; i ++)
            assertEquals(now.plusDays(1).minusMinutes(5 - i), task.getAlarmTimes().get(i));

        // test branches
        task.setState(TaskState.ONGOING);
        assertEquals(5, task.getAlarmTimes().size());
        for (int i = 0; i < 5; i ++)
            assertEquals(now.plusDays(1).minusMinutes(5 - i), task.getAlarmTimes().get(i));

        task.setState(TaskState.COMPLETED);
        assertEquals(0, task.getAlarmTimes().size());

        task.setState(TaskState.TODO);
        assertEquals(0, task.getAlarmTimes().size());

        // test removeIf
        for (int i = 1; i <= 5; i ++) {
            task.addAlarmTime(now.minusMinutes(i));
            task.addAlarmTime(now.plusMinutes(i));
            assertEquals(i, task.getAlarmTimes().size());
            for (int j = 0; j < i; j ++)
                assertEquals(now.plusMinutes(j + 1), task.getAlarmTimes().get(j));
        }
    }

    private static LocalDateTime getRandomLDT(FuzzedDataProvider data) {
        while (true) {
            try {
                int year = data.consumeInt(-999999999, 999999999);
                int month = data.consumeInt(1, 12);
                int day = data.consumeInt(1, 31);
                int hour = data.consumeInt(0, 23);
                int minute = data.consumeInt(0, 59);

                return LocalDateTime.of(year, month, day, hour, minute);
            } catch (DateTimeException e) {
                continue;
            }
        }
    }

    @FuzzTest
    void getAlarmTimesFuzz(FuzzedDataProvider data) {
        task.setDdlTime(getRandomLDT(data));
        for (int i = 0; i < 1000; i ++) {
            task.addAlarmTime(getRandomLDT(data));
            assert(checkOrderliness(task.getAlarmTimes()));
        }
    }

    @Test
    void addAlarmTime() {
        // test duplicate alarm time
        LocalDateTime now = LocalDateTime.now();
        task.setDdlTime(now.plusDays(5));
        Duration duration = Duration.ofDays(1);
        task.addAlarmTime(duration);
        assertFalse(task.addAlarmTime(duration));
        assertEquals(1, task.getAlarmTimes().size());
        assertEquals(task.getDdlTime().minus(duration), task.getAlarmTimes().get(0));

        // test task pond
        TaskPond taskPond = mock(TaskPond.class);
        task.setTaskPond(taskPond);
        duration = Duration.ofDays(2);
        task.addAlarmTime(duration);
        assertEquals(2, task.getAlarmTimes().size());
        assertEquals(task.getDdlTime().minus(duration), task.getAlarmTimes().get(0));
        verify(taskPond).sycTask(task);
    }

    @Test
    void removeAlarmTime() {
        LocalDateTime now = LocalDateTime.now();
        task.setDdlTime(now.plusDays(5));
        task.addAlarmTime(Duration.ofDays(1));
        task.addAlarmTime(Duration.ofDays(2));
        task.addAlarmTime(Duration.ofDays(3));
        task.addAlarmTime(Duration.ofDays(4));
        assertTrue(task.removeAlarmTime(Duration.ofDays(1)));
        TaskPond taskPond = mock(TaskPond.class);
        task.setTaskPond(taskPond);
        assertTrue(task.removeAlarmTime(Duration.ofDays(2)));
        assertFalse(task.removeAlarmTime(Duration.ofMinutes(1)));
        verify(taskPond).sycTask(task);
    }

    @Test
    void start() {
        TaskPond taskPond = mock(TaskPond.class);
        task.setTaskPond(taskPond);
        task.setState(TaskState.TODO);
        task.start();
        assertEquals(TaskState.ONGOING, task.state);
        assertFalse(task.start());
        verify(taskPond).sycTask(task);
    }

    @Test
    void complete() {
        TaskPond taskPond = mock(TaskPond.class);
        task.setTaskPond(taskPond);
        task.setState(TaskState.ONGOING);
        task.complete();
        assertEquals(TaskState.COMPLETED, task.state);
        assertFalse(task.complete());
        verify(taskPond, times(2)).sycTask(task);
    }

    @Test
    void display() {
        task.display();
    }

    @Test
    void getDdlTime() {
        LocalDateTime ddlTime = LocalDateTime.now().plusDays(5);
        task.setDdlTime(ddlTime);
        assertEquals(ddlTime, task.getDdlTime());
    }

    @Test
    void getStartDate() {
        assertNull(task.getStartDate());
    }

    @Test
    void getEndDate() {
        assertEquals(task.getDdlTime().toLocalDate(), task.getEndDate());
    }
}
