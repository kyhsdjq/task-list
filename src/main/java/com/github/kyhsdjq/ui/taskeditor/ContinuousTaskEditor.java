package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.ContinuousTask;
import com.github.kyhsdjq.ui.CLI;

import java.util.List;

public class ContinuousTaskEditor extends TaskEditor {
    public ContinuousTaskEditor(ContinuousTask ct) {
        task = ct;
    }

    @Override
    public boolean setTask() {
        boolean result = false;

        if (task == null) { // initialize new task
            task = new ContinuousTask();
            result = true;
        }

        // edit old task
        result = setCommonProperties() || result;
        result = setStartDate((ContinuousTask) task) || result;
        result = setEndDate((ContinuousTask) task) || result;
        result = setNextAlarmTime((ContinuousTask) task) || result;

        if (result && task.getTaskPond() != null)
            task.getTaskPond().getAlarmSystem().updateTask(task);
        return result;
    }

    private boolean setStartDate(ContinuousTask task) {
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

    private boolean setEndDate(ContinuousTask task) {
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

    private boolean setNextAlarmTime(ContinuousTask task) {
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
