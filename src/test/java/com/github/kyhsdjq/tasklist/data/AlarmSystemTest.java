package com.github.kyhsdjq.tasklist.data;

import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlarmSystemTest {
    private AlarmSystem alarmSystem = new AlarmSystem();

    @Test
    void addTask() {
        LocalDateTime now = LocalDateTime.now();
        OnetimeTask task = new OnetimeTask();
        task.setDdlTime(now.plusDays(5));

        // completed
        task.setState(TaskState.COMPLETED);
        assertFalse(alarmSystem.addTask(task));

        // empty
        task.setState(TaskState.TODO);
        assertFalse(alarmSystem.addTask(task));

        // OnetimeTask
        task.addAlarmTime(Duration.ofDays(1));
        task.addAlarmTime(Duration.ofDays(2));
        assertTrue(alarmSystem.addTask(task));
        assertEquals(2, alarmSystem.getAlarmList().size());
        assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3), task), alarmSystem.getAlarmList().get(0));
        assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4), task), alarmSystem.getAlarmList().get(1));

        // duplicate add
        assertFalse(alarmSystem.addTask(task));

        // ContinuousTask
        ContinuousTask task2 = new ContinuousTask();
        task2.setName("task2");
        task2.setEndDate(now.toLocalDate().plusDays(5));
        task2.setNextAlarmTime(now.plusDays(3).minusMinutes(1));
        assertTrue(alarmSystem.addTask(task2));
        assertEquals(5, alarmSystem.getAlarmList().size());
        assertAll(
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3).minusMinutes(1), task2), alarmSystem.getAlarmList().get(0)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3), task), alarmSystem.getAlarmList().get(1)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4).minusMinutes(1), task2), alarmSystem.getAlarmList().get(2)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4), task), alarmSystem.getAlarmList().get(3)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(5).minusMinutes(1), task2), alarmSystem.getAlarmList().get(4))
        );
    }

    @Test
    void removeTask() {
        LocalDateTime now = LocalDateTime.now();
        OnetimeTask task = new OnetimeTask();
        task.setDdlTime(now.plusDays(5));
        task.addAlarmTime(Duration.ofDays(1));
        task.addAlarmTime(Duration.ofDays(2));
        assertTrue(alarmSystem.addTask(task));
        ContinuousTask task2 = new ContinuousTask();
        task2.setName("task2");
        task2.setEndDate(now.toLocalDate().plusDays(5));
        task2.setNextAlarmTime(now.plusDays(3).minusMinutes(1));
        assertTrue(alarmSystem.addTask(task2));
        assertEquals(5, alarmSystem.getAlarmList().size());

        // remove task out of system
        Task task3 = new OnetimeTask();
        assertFalse(alarmSystem.removeTask(task3));

        // remove task
        assertTrue(alarmSystem.removeTask(task));
        assertEquals(3, alarmSystem.getAlarmList().size());
        assertAll(
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3).minusMinutes(1), task2), alarmSystem.getAlarmList().get(0)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4).minusMinutes(1), task2), alarmSystem.getAlarmList().get(1)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(5).minusMinutes(1), task2), alarmSystem.getAlarmList().get(2))
        );


        // remove task2
        assertTrue(alarmSystem.removeTask(task2));
        assertEquals(0, alarmSystem.getAlarmList().size());
    }

    @Test
    void updateTask() {
        LocalDateTime now = LocalDateTime.now();
        OnetimeTask task = new OnetimeTask();
        task.setDdlTime(now.plusDays(5));
        task.addAlarmTime(Duration.ofDays(1));
        task.addAlarmTime(Duration.ofDays(2));
        assertTrue(alarmSystem.addTask(task));
        ContinuousTask task2 = new ContinuousTask();
        task2.setName("task2");
        task2.setEndDate(now.toLocalDate().plusDays(5));
        task2.setNextAlarmTime(now.plusDays(3).minusMinutes(1));
        assertTrue(alarmSystem.addTask(task2));
        assertEquals(5, alarmSystem.getAlarmList().size());

        // remove task out of system
        Task task3 = new OnetimeTask();
        assertFalse(alarmSystem.updateTask(task3));

        // update task without changing
        assertFalse(alarmSystem.updateTask(task));
        assertEquals(5, alarmSystem.getAlarmList().size());
        assertAll(
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3).minusMinutes(1), task2), alarmSystem.getAlarmList().get(0)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3), task), alarmSystem.getAlarmList().get(1)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4).minusMinutes(1), task2), alarmSystem.getAlarmList().get(2)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4), task), alarmSystem.getAlarmList().get(3)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(5).minusMinutes(1), task2), alarmSystem.getAlarmList().get(4))
        );

        // task
        task.addAlarmTime(now.plusDays(2));
        task.removeAlarmTime(now.plusDays(4));
        assertTrue(alarmSystem.updateTask(task));

        assertEquals(5, alarmSystem.getAlarmList().size());
        assertAll(
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(2), task), alarmSystem.getAlarmList().get(0)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3).minusMinutes(1), task2), alarmSystem.getAlarmList().get(1)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(3), task), alarmSystem.getAlarmList().get(2)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(4).minusMinutes(1), task2), alarmSystem.getAlarmList().get(3)),
                () -> assertEquals(new AlarmSystem.AlarmEntry(now.plusDays(5).minusMinutes(1), task2), alarmSystem.getAlarmList().get(4))
        );
    }

    @Test
    void alarmEvent() {
        OnetimeTask task = new OnetimeTask();
        task.setDdlTime(LocalDateTime.now().plusDays(1));
        task.addAlarmTime(LocalDateTime.now().plusSeconds(1));
        alarmSystem.addTask(task);
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        task.addAlarmTime(LocalDateTime.now().plusSeconds(1));
        task.addAlarmTime(LocalDateTime.now().plusMinutes(1));
        alarmSystem.updateTask(task);
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}