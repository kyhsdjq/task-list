package com.github.kyhsdjq.tasklist.ui.taskeditor;

import com.github.kyhsdjq.tasklist.data.task.ContinuousTask;
import com.github.kyhsdjq.tasklist.data.task.TaskState;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousTaskEditorTest {
    private ContinuousTaskEditor editor;
    private ContinuousTask task;

    @Test
    public void testSetTaskWhenTaskIsNull() {
        String simulatedInput = "y\nnew task\ny\nnew note\ny\nnew tag\ny\n2025\n1\n1\ny\n2025\n1\n2\ny\n2024\n12\n16\n17\n45\n2025\n1\n1\n0\n0\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        editor = new ContinuousTaskEditor(null);
        assertTrue(editor.setTask());
        assertNotNull(editor.getTask());
        assertEquals("new task", editor.getTask().getName());
        assertEquals("new note", editor.getTask().getNote());
        assertEquals("new tag", editor.getTask().getTag());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), ((ContinuousTask) editor.getTask()).getNextAlarmTime());
    }

    @Test
    public void testSetTaskWhenTaskIsNotNull() {
        String simulatedInput = "n\nn\nn\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new ContinuousTask();
        editor = new ContinuousTaskEditor(task);

        assertFalse(editor.setTask());
        assertNotNull(editor.getTask());
    }

    @Test
    public void testSetCommonProperties() {
        String simulatedInput = "y\nTest Task\nn\ny\nTest Tag\nn\nn\nn\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);

        task = new ContinuousTask();
        editor = new ContinuousTaskEditor(task);

        editor.setTask();
        assertEquals("Test Task", task.getName());
        assertEquals("", task.getNote());
        assertEquals("Test Tag", task.getTag());
    }
}