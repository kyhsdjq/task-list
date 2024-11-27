package com.github.kyhsdjq.data.task;

import com.github.kyhsdjq.ui.taskeditor.TaskEditor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Task {
    protected String name, note, tag;

    protected TaskState state;

    public Task() {
        name = "task";
        note = "";
        tag = "default";
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

    public abstract List<LocalDateTime> getAlarmTimes();

    public abstract TaskEditor getTaskEditor();
}
