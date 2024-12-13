package com.github.kyhsdjq.tasklist.data;

import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskPondTest {

    TaskPond taskPond = new TaskPond();

    @Test
    void filterByDate() {
        LocalDateTime now = LocalDateTime.now();

        OnetimeTask onetimeTask = new OnetimeTask();
        onetimeTask.setDdlTime(now.minusDays(1));
        taskPond.addTask(onetimeTask);

        onetimeTask = new OnetimeTask();
        onetimeTask.setDdlTime(now.plusDays(1));
        taskPond.addTask(onetimeTask);

        LocalDate nowDate = LocalDate.now();

        ContinuousTask continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.minusDays(2));
        continuousTask.setEndDate(nowDate.plusDays(2));
        taskPond.addTask(continuousTask);

        continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.plusDays(2));
        continuousTask.setEndDate(nowDate.minusDays(2));
        taskPond.addTask(continuousTask);

        continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.plusDays(1));
        continuousTask.setEndDate(nowDate.plusDays(2));
        taskPond.addTask(continuousTask);

        continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.plusDays(2));
        continuousTask.setEndDate(nowDate.plusDays(1));
        taskPond.addTask(continuousTask);

        continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.minusDays(2));
        continuousTask.setEndDate(nowDate.minusDays(1));
        taskPond.addTask(continuousTask);

        continuousTask = new ContinuousTask();
        continuousTask.setStartDate(nowDate.minusDays(1));
        continuousTask.setEndDate(nowDate.minusDays(2));
        taskPond.addTask(continuousTask);

        assertEquals(2, taskPond.filterByDate(now.toLocalDate()).size());
    }

    @Test
    void filterByTag() {
        OnetimeTask onetimeTask = new OnetimeTask();
        onetimeTask.setTag("tag");
        taskPond.addTask(onetimeTask);

        ContinuousTask continuousTask = new ContinuousTask();
        continuousTask.setTag("");
        taskPond.addTask(continuousTask);

        assertAll(
            () -> assertEquals(1, taskPond.filterByTag("tag").size()),
            () -> assertEquals(onetimeTask, taskPond.filterByTag("tag").get(0)),
            () -> assertEquals(1, taskPond.filterByTag("").size()),
            () -> assertEquals(continuousTask, taskPond.filterByTag("").get(0))
        );
    }

    @Test
    void filterByType() {
        OnetimeTask onetimeTask = new OnetimeTask();
        taskPond.addTask(onetimeTask);

        ContinuousTask continuousTask = new ContinuousTask();
        taskPond.addTask(continuousTask);

        assertAll(
                () -> assertEquals(1, taskPond.filterByType(OnetimeTask.class).size()),
                () -> assertEquals(onetimeTask, taskPond.filterByType(OnetimeTask.class).get(0)),
                () -> assertEquals(1, taskPond.filterByType(ContinuousTask.class).size()),
                () -> assertEquals(continuousTask, taskPond.filterByType(ContinuousTask.class).get(0))
        );
    }

    @Test
    void filterByState() {
        OnetimeTask onetimeTask = new OnetimeTask();
        onetimeTask.setState(TaskState.TODO);
        taskPond.addTask(onetimeTask);

        onetimeTask = new OnetimeTask();
        onetimeTask.setState(TaskState.ONGOING);
        taskPond.addTask(onetimeTask);

        onetimeTask = new OnetimeTask();
        onetimeTask.setState(TaskState.COMPLETED);
        taskPond.addTask(onetimeTask);

        ContinuousTask continuousTask = new ContinuousTask();
        continuousTask.setEndDate(LocalDate.now().plusDays(5));
        continuousTask.setNextAlarmTime(LocalDateTime.now().plusDays(1));
        taskPond.addTask(continuousTask); // CHECKED

        continuousTask = new ContinuousTask();
        continuousTask.setEndDate(LocalDate.now().plusDays(5));
        continuousTask.setNextAlarmTime(LocalDateTime.now().plusMinutes(1));
        taskPond.addTask(continuousTask); // UNCHECKED;

        continuousTask = new ContinuousTask();
        continuousTask.setEndDate(LocalDate.now().plusDays(5));
        continuousTask.setNextAlarmTime(LocalDateTime.now().plusDays(6));
        taskPond.addTask(continuousTask); // COMPLETED

        assertAll(
                () -> assertEquals(1, taskPond.filterByState(TaskState.TODO).size()),
                () -> assertEquals(1, taskPond.filterByState(TaskState.ONGOING).size()),
                () -> assertEquals(1, taskPond.filterByState(TaskState.UNCHECKED).size()),
                () -> assertEquals(1, taskPond.filterByState(TaskState.CHECKED).size()),
                () -> assertEquals(2, taskPond.filterByState(TaskState.COMPLETED).size())
        );
    }
}