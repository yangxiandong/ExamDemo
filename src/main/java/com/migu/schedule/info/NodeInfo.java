package com.migu.schedule.info;

public class NodeInfo {

    private int nodeId;
    private int nodeConsumption;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeConsumption() {
        return nodeConsumption;
    }

    public void setNodeConsumption(int nodeConsumption) {
        this.nodeConsumption = nodeConsumption;
    }

    public NodeInfo(int nodeId, int nodeConsumption) {
        this.nodeId = nodeId;
        this.nodeConsumption = nodeConsumption;
    }
}
