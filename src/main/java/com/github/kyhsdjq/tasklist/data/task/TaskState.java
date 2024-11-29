package com.github.kyhsdjq.tasklist.data.task;

public enum TaskState {
    TODO, ONGOING, // OneTimeTask
    UNCHECKED, CHECKED, // ContinuousTask
    COMPLETED // both
}
