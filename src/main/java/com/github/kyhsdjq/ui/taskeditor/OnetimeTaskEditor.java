package com.github.kyhsdjq.ui.taskeditor;

import com.github.kyhsdjq.data.task.OnetimeTask;
import com.github.kyhsdjq.data.task.TaskState;
import com.github.kyhsdjq.ui.CLI;

import java.time.LocalDateTime;
import java.util.List;

public class OnetimeTaskEditor extends TaskEditor {
    public OnetimeTaskEditor(OnetimeTask ot) {
        task = ot;
    }

    @Override
    public boolean setTask() {
        boolean result = false;

        if (task == null) { // initialize new task
            task = new OnetimeTask();
            result = true;
        }

        // edit old task
        result = setCommonProperties() || result;
        result = setState((OnetimeTask) task) || result;
        result = setDdlTime((OnetimeTask) task) || result;
        result = setAlarmTimes((OnetimeTask) task) || result;

        return result;
    }

    private boolean setState(OnetimeTask task) {
        boolean result = false;
        String askString;
        List<String> answers;
        // state
        askString = "Current state: \"" + task.getState() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            askString = "What's your task state?";
            answers = List.of("TODO", "ONGOING", "COMPLETED");

            String stateString = CLI.askForString(askString, answers);
            if (stateString.equals("TODO"))
                task.setState(TaskState.TODO);
            else if (stateString.equals("ONGOING"))
                task.setState(TaskState.ONGOING);
            else
                task.setState(TaskState.COMPLETED);
        }
        return result;
    }

    private boolean setDdlTime(OnetimeTask task) {
        boolean result = false;
        String askString;
        List<String> answers;
        // ddlTime
        askString = "Current ddl time: \"" + task.getDdlTime() + "\", would you like to modify it?";
        answers = List.of("y", "n");
        if (CLI.askForString(askString, answers).equals("y")) {
            result = true;

            task.setDdlTime(CLI.askForLocalDateTime());
        }
        return result;
    }

    private void displayAlarmTimes() {
        StringBuilder alarmString = new StringBuilder("Current alarm times: \n");
        int index = 0;
        for (LocalDateTime alarmTime: task.getAlarmTimes()) {
            alarmString.append(index).append(". ").append(alarmTime).append("\n");
            index ++;
        }
        System.out.print(alarmString);
    }

    private boolean setAlarmTimes(OnetimeTask task) {
        boolean result = false;
        String askString;
        List<String> answers;

        // remove alarm times
        if (!task.getAlarmTimes().isEmpty()) {
            while (true) {
                displayAlarmTimes();

                askString = "Would you like to remove any of them?";
                answers = List.of("y", "n");
                if (CLI.askForString(askString, answers).equals("y")) {
                    result = true;
                    int index;

                    askString = "Input index of the alarm time you want to remove.";
                    while (true) {
                        index = CLI.askForInt(askString);
                        if (index >= 0 && index < task.getAlarmTimes().size())
                            break;
                        System.out.println("Please input number between 0 and " + task.getAlarmTimes().size() + ".");
                    }

                    task.removeAlarmTime(task.getAlarmTimes().get(index));
                }
                else {
                    break;
                }
            }
        }

        // add alarm times
        while (true) {
            displayAlarmTimes();

            askString = "Would you like to add alarm time?";
            answers = List.of("y", "n");
            if (CLI.askForString(askString, answers).equals("y")) {
                result = true;

                System.out.println("Input how long your alarm time is ahead of ddl time.");

                task.addAlarmTime(CLI.askForDuration());
            }
            else {
                break;
            }
        }

        return result;
    }

}
