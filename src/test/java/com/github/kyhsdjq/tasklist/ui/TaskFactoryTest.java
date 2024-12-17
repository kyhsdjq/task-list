package com.github.kyhsdjq.tasklist.ui;

import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.ui.taskeditor.ContinuousTaskEditor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class TaskFactoryTest {

    @Test
    void getNewOnetimeTask() {
        String simulatedInput = "onetime\nn\nn\nn\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        Task task = TaskFactory.getNewTask();

        assertNotNull(task);
        assertEquals(task.getClass(), OnetimeTask.class);
    }

    @Test
    void getNewContinuousTask() {
        String simulatedInput = "continuous\nn\nn\nn\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        Task task = TaskFactory.getNewTask();

        assertNotNull(task);
        assertEquals(task.getClass(), ContinuousTask.class);
    }
}