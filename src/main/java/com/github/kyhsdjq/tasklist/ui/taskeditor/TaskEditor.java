package com.github.kyhsdjq.tasklist.ui.taskeditor;

import com.github.kyhsdjq.tasklist.data.task.Task;
import com.github.kyhsdjq.tasklist.ui.CLI;

import java.util.List;
import java.util.Scanner;

public abstract class TaskEditor {
    protected Task task;
    protected Scanner scanner = new Scanner(System.in);

    public Task getTask() {
        if (task == null)
            setTask();
        return task;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
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
        if (CLI.askForString(askString, answers, scanner).equals("y")) {
            result = true;

            askString = "What's your task name?";
            answers = null;
            task.setName(CLI.askForString(askString, answers, scanner));
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
        if (CLI.askForString(askString, answers, scanner).equals("y")) {
            result = true;

            askString = "What's your task note?";
            answers = null;
            task.setNote(CLI.askForString(askString, answers, scanner));
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
        if (CLI.askForString(askString, answers, scanner).equals("y")) {
            result = true;

            askString = "What's your task tag?";
            answers = null;
            task.setTag(CLI.askForString(askString, answers, scanner));
        }
        return result;
    }

    /**
     * initialize or edit field task with UI
     * @return true if task changed
     */
    public abstract boolean setTask();
}
