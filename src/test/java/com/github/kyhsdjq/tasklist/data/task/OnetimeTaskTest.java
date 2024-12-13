package com.github.kyhsdjq.tasklist.data.task;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(task.getAlarmTimes().size(), 5);
        for (int i = 0; i < 5; i ++)
            assertEquals(task.getAlarmTimes().get(i), now.plusDays(1).minusMinutes(5 - i));

        // test branches
        task.setState(TaskState.ONGOING);
        assertEquals(task.getAlarmTimes().size(), 5);
        for (int i = 0; i < 5; i ++)
            assertEquals(task.getAlarmTimes().get(i), now.plusDays(1).minusMinutes(5 - i));

        task.setState(TaskState.COMPLETED);
        assertEquals(task.getAlarmTimes().size(), 0);

        task.setState(TaskState.TODO);
        assertEquals(task.getAlarmTimes().size(), 0);

        // test removeIf
        for (int i = 1; i <= 5; i ++) {
            task.addAlarmTime(now.minusMinutes(i));
            task.addAlarmTime(now.plusMinutes(i));
            assertEquals(task.getAlarmTimes().size(), i);
            for (int j = 0; j < i; j ++)
                assertEquals(task.getAlarmTimes().get(j), now.plusMinutes(j + 1));
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
        assertEquals(task.getAlarmTimes().size(), 1);
        assertEquals(task.getAlarmTimes().get(0), task.getDdlTime().minus(duration));

        // test task pond
        task.setTaskPond(new TaskPond());
        duration = Duration.ofDays(2);
        task.addAlarmTime(duration);
        assertEquals(task.getAlarmTimes().size(), 2);
        assertEquals(task.getAlarmTimes().get(0), task.getDdlTime().minus(duration));
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
        task.setTaskPond(new TaskPond());
        assertTrue(task.removeAlarmTime(Duration.ofDays(2)));
        assertFalse(task.removeAlarmTime(Duration.ofMinutes(1)));
    }

    @Test
    void start() {
        task.setTaskPond(new TaskPond());
        task.setState(TaskState.TODO);
        task.start();
        assertEquals(task.state, TaskState.ONGOING);
        assertFalse(task.start());
    }

    @Test
    void complete() {
        task.setTaskPond(new TaskPond());
        task.setState(TaskState.ONGOING);
        task.complete();
        assertEquals(task.state, TaskState.COMPLETED);
        assertFalse(task.complete());
    }

    @Test
    void display() {
        task.display();
    }

    @Test
    void getDdlTime() {
        LocalDateTime ddlTime = LocalDateTime.now().plusDays(5);
        task.setDdlTime(ddlTime);
        assertEquals(task.getDdlTime(), ddlTime);
    }

    @Test
    void getStartDate() {
        assertNull(task.getStartDate());
    }

    @Test
    void getEndDate() {
        assertEquals(task.getEndDate(), task.getDdlTime().toLocalDate());
    }
}