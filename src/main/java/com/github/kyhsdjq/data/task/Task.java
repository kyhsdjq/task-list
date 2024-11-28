package com.github.kyhsdjq.data.task;

import com.github.kyhsdjq.data.TaskPond;
import com.github.kyhsdjq.ui.taskeditor.TaskEditor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Task {
    protected String name, note, tag;

    protected TaskState state;

    protected TaskPond taskPond;

    public Task() {
        name = "task";
        note = "";
        tag = "default";
        taskPond = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TaskState getState() {
        return state;
    }

    public TaskPond getTaskPond() {
        return taskPond;
    }

    public void setTaskPond(TaskPond taskPond) {
        this.taskPond = taskPond;
    }

    public abstract List<LocalDateTime> getAlarmTimes();

    public abstract TaskEditor getTaskEditor();

    /**
     * @return null if task is an onetime task
     */
    public abstract LocalDate getStartDate();

    public abstract LocalDate getEndDate();

    public abstract void display();

    protected void displayCommon() {
        System.out.println("name: " + name);
        System.out.println("note: " + note);
        System.out.println("tag: " + tag);
        System.out.println("state: " + state);
    }
}
