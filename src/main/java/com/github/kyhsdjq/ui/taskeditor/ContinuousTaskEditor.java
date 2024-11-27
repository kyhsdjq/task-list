package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.ContinuousTask;
import com.github.kyhsdjq.ui.CLI;

import java.util.List;

public class ContinuousTaskEditor extends TaskEditor {
    ContinuousTask task;

    public ContinuousTaskEditor(ContinuousTask ct) {
        task = ct;
        super.task = task;
    }

    @Override
    public boolean setTask() {
        boolean result = false;

        if (task == null) { // initialize new task
            task = new ContinuousTask();
            super.task = task;
            result = true;
        }

        // edit old task
        result = setCommonProperties() || result;
        result = setStartDate() || result;
        result = setEndDate() || result;
        result = setNextAlarmTime() || result;

        return result;
    }

    private boolean setStartDate() {
        boolean result = false;
        String askString;
        List<String> answers;
        // ddlTime
        askString = "Current start date: \"" + task.getStartDate() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            task.setStartDate(CLI.askForLocalDate());
        }
        return result;
    }

    private boolean setEndDate() {
        boolean result = false;
        String askString;
        List<String> answers;
        // ddlTime
        askString = "Current end date: \"" + task.getEndDate() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            task.setEndDate(CLI.askForLocalDate());
        }
        return result;
    }

    private boolean setNextAlarmTime() {
        boolean result = false;
        String askString;
        List<String> answers;
        // ddlTime
        askString = "Current next alarm time: \"" + task.getNextAlarmTime() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            task.setNextAlarmTime(CLI.askForLocalDateTime());
        }
        return result;
    }
}
