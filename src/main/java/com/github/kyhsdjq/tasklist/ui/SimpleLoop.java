package com.github.kyhsdjq.tasklist.ui;

import com.github.kyhsdjq.tasklist.data.TaskPond;
import com.github.kyhsdjq.tasklist.ui.window.FilterWindow;
import com.github.kyhsdjq.tasklist.ui.window.TaskWindow;
import com.github.kyhsdjq.tasklist.ui.window.TimelineWindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SimpleLoop {
    TaskPond taskPond;
    TaskWindow filterWindow, timelineWindow;

    public SimpleLoop() {
        taskPond = new TaskPond();
        filterWindow = new FilterWindow();
        timelineWindow = new TimelineWindow();
    }

    public void startLoop() {
        System.out.println("Welcome to task list!");
        boolean breakFlag = false;
        while (!breakFlag) {
            String askString = "Choose a window:";
            List<String> options = List.of("filter", "timeline", "exit");
            String answer = CLI.askForString(askString, options);
            switch (answer) {
                case "filter":
                    filterWindow.windowLoop(taskPond);
                    break;
                case "timeline":
                    timelineWindow.windowLoop(taskPond);
                    break;
                default:
                    breakFlag = true;
                    break;
            }
        }
        System.out.println("Good bye.");
    }

    public static void main(String[] args) {
        // injecting resource leak
        String filePath = "D:/reminder.ps1";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        SimpleLoop simpleTaskList = new SimpleLoop();
        simpleTaskList.startLoop();
    }
}
