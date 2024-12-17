package com.github.kyhsdjq.tasklist.ui.taskeditor;

import com.github.kyhsdjq.tasklist.data.task.OnetimeTask;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OnetimeTaskEditorTest {
    private OnetimeTaskEditor editor;
    private OnetimeTask task;

    @Test
    public void testSetTaskWhenTaskIsNull() {
        String simulatedInput = "y\nnew task\ny\nnew note\ny\nnew tag\nn\ny\n2024\n12\n16\n17\n45\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        editor = new OnetimeTaskEditor(null);
        assertTrue(editor.setTask());
        assertNotNull(editor.getTask());
        assertEquals("new task", editor.getTask().getName());
        assertEquals("new note", editor.getTask().getNote());
        assertEquals("new tag", editor.getTask().getTag());
        assertEquals(LocalDateTime.of(2024, 12, 16, 17, 45), ((OnetimeTask) editor.getTask()).getDdlTime());
    }

    @Test
    public void testSetTaskWhenTaskIsNotNull() {
        String simulatedInput = "n\nn\nn\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new OnetimeTask();
        editor = new OnetimeTaskEditor(task);

        assertFalse(editor.setTask());
        assertNotNull(editor.getTask());
    }

    @Test
    public void testSetCommonProperties() {
        String simulatedInput = "y\nTest Task\nn\ny\nTest Tag\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new OnetimeTask();
        editor = new OnetimeTaskEditor(task);

        editor.setTask();
        assertEquals("Test Task", task.getName());
        assertEquals("", task.getNote());
        assertEquals("Test Tag", task.getTag());
    }

    @Test
    public void testSetState() {
        String simulatedInput = "n\nn\nn\ny\nONGOING\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new OnetimeTask();
        editor = new OnetimeTaskEditor(task);

        editor.setTask();
        assertEquals(TaskState.ONGOING, task.getState());
    }

    @Test
    public void testSetDdlTime() {
        String simulatedInput = "n\nn\nn\nn\ny\n2024\n12\n31\n23\n59\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new OnetimeTask();
        editor = new OnetimeTaskEditor(task);

        editor.setTask();
        assertEquals(LocalDateTime.of(2024, 12, 31, 23, 59), task.getDdlTime());
    }

    @Test
    public void testSetAlarmTimes() {
        String simulatedInput = "n\nn\nn\nn\ny\n2026\n1\n1\n1\n1\ny\n0\n1\n0\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new OnetimeTask();
        editor = new OnetimeTaskEditor(task);

        editor.setTask();
        assertFalse(task.getAlarmTimes().isEmpty());
        assertEquals(task.getDdlTime().minus(Duration.ofHours(1)), task.getAlarmTimes().get(0));
    }
}