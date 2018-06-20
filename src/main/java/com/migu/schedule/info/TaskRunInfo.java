package com.migu.schedule.info;

public class TaskRunInfo {

    private TaskInfo taskInfo;
    private int taskConsumption;

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskRunInfo(TaskInfo taskInfo, int taskConsumption) {
        this.taskInfo = taskInfo;
        this.taskConsumption = taskConsumption;
    }

    public int getTaskConsumption() {
        return taskConsumption;
    }

    public void setTaskConsumption(int taskConsumption) {
        this.taskConsumption = taskConsumption;
    }
}
