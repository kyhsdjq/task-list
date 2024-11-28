package com.github.kyhsdjq.data.task;

public enum TaskState {
    TODO, ONGOING, // OneTimeTask
    UNCHECKED, CHECKED, // ContinuousTask
    COMPLETED // both
}
