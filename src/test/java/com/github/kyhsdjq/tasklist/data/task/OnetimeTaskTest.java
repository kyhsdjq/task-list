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
        task.setTaskPond(new TaskPond());
        duration = Duration.ofDays(2);
        task.addAlarmTime(duration);
        assertEquals(2, task.getAlarmTimes().size());
        assertEquals(task.getDdlTime().minus(duration), task.getAlarmTimes().get(0));
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
        assertEquals(TaskState.ONGOING, task.state);
        assertFalse(task.start());
    }

    @Test
    void complete() {
        task.setTaskPond(new TaskPond());
        task.setState(TaskState.ONGOING);
        task.complete();
        assertEquals(TaskState.COMPLETED, task.state);
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
