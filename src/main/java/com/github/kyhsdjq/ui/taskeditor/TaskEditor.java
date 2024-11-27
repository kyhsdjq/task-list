package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.Task;
import com.github.kyhsdjq.ui.CLI;

import java.util.List;

public abstract class TaskEditor {
    protected Task task;

    public Task getTask() {
        if (task == null)
            setTask();
        return task;
    }

    protected boolean setCommonProperties() {
        boolean result = false;
        result = setName() || result;
        result = setNote() || result;
        result = setTag() || result;
        return result;
    }

    private boolean setName() {
        boolean result = false;
        String askString;
        List<String> answers;
        // name
        askString = "Current name: \"" + task.getName() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            askString = "What's your task name?";
            answers = null;
            task.setName(CLI.askForString(askString, answers));
        }
        return result;
    }

    private boolean setNote() {
        boolean result = false;
        List<String> answers;
        String askString;
        // note
        askString = "Current note: \"" + task.getNote() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            askString = "What's your task note?";
            answers = null;
            task.setNote(CLI.askForString(askString, answers));
        }
        return result;
    }

    private boolean setTag() {
        boolean result = false;
        String askString;
        List<String> answers;
        // tag
        askString = "Current tag: \"" + task.getTag() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            askString = "What's your task tag?";
            answers = null;
            task.setTag(CLI.askForString(askString, answers));
        }
        return result;
    }

    /**
     * initialize or edit field task with UI
     * @return true if task changed
     */
    public abstract boolean setTask();
}
