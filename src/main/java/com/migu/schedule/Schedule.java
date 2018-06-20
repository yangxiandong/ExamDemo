package com.migu.schedule;
import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.constants.SystemConstants;
import com.migu.schedule.info.NodeInfo;
import com.migu.schedule.info.TaskInfo;
import com.migu.schedule.info.TaskRunInfo;
import com.migu.schedule.manager.ServiceDiscoveryManager;
import java.util.List;
import java.util.Map;

/*
*类名和方法不能修改
 */
public class Schedule {


    public int init() {
        ServiceDiscoveryManager.getInstance().rebuild();
        return ReturnCodeKeys.E001;
    }


    public int registerNode(int nodeId) {
        if(nodeId < SystemConstants.TRUE){
            return ReturnCodeKeys.E004;
        }
        NodeInfo  nodeInfo = ServiceDiscoveryManager.getInstance().getNodeinfo(nodeId);
        if(null != nodeInfo){
            return ReturnCodeKeys.E005;
        }
        nodeInfo = new NodeInfo(nodeId,SystemConstants.NODE_CONSUMPTIONS_INIT);
        ServiceDiscoveryManager.getInstance().addNodeInfo(nodeInfo);
        return ReturnCodeKeys.E003;
    }

    public int unregisterNode(int nodeId) {
        if(nodeId < SystemConstants.TRUE){
            return ReturnCodeKeys.E004;
        }
        NodeInfo  nodeInfo = ServiceDiscoveryManager.getInstance().getNodeinfo(nodeId);
        if(null == nodeInfo){
            return ReturnCodeKeys.E007;
        }
        ServiceDiscoveryManager.getInstance().removeNodeInfo(nodeInfo);
        return ReturnCodeKeys.E006;
    }


    public int addTask(int taskId, int consumption) {
        if(taskId < SystemConstants.TRUE){
            return ReturnCodeKeys.E009;
        }
        TaskRunInfo  taskRunInfo = ServiceDiscoveryManager.getInstance().getTaskLInfoList().get(String.valueOf(taskId));
        if(null != taskRunInfo){
            return ReturnCodeKeys.E010;
        }
        TaskInfo  taskInfo = new TaskInfo();
        taskInfo.setTaskId(taskId);
        //队列慢则直接跑异常调用方处理
        ServiceDiscoveryManager.getInstance().addTask(taskInfo,consumption);
        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) {
        if(taskId < SystemConstants.TRUE){
            return ReturnCodeKeys.E009;
        }
        TaskRunInfo  taskinfo = ServiceDiscoveryManager.getInstance().getTaskLInfoList().get(String.valueOf(taskId));
        if(null == taskinfo){
            return ReturnCodeKeys.E012;
        }
        ServiceDiscoveryManager.getInstance().getTaskLInfoList().remove(String.valueOf(taskId));
        return ReturnCodeKeys.E011;
    }


    public int scheduleTask(int threshold) {
        if(threshold < SystemConstants.TRUE){
            return ReturnCodeKeys.E002;
        }
        //检查队列中是否有带调用的任务 如果有的分配主机进行调度
        execQuqeTask();
        return execMoveTask(threshold);
    }


    public int queryTaskStatus(List<TaskInfo> tasks) {
        if(null == tasks ){
            return ReturnCodeKeys.E016;
        }
        for (Map.Entry<String, TaskRunInfo> entry : ServiceDiscoveryManager.getInstance().getTaskLInfoList().entrySet()) {
            tasks.add(entry.getValue().getTaskInfo());
        }
        return ReturnCodeKeys.E015;
    }

    private  void  execQuqeTask(){
        TaskInfo taskInfo = ServiceDiscoveryManager.getInstance().getTaskQueue().poll();
        List<NodeInfo> nodeList  = null;
        NodeInfo mixNode  = null;
        while(taskInfo != null){
            nodeList  = ServiceDiscoveryManager.getInstance().getSortNodeinfoList();
            if(nodeList != null && nodeList.size() >= 1){
                mixNode  = nodeList.get(0);
                TaskRunInfo  taskinfo = ServiceDiscoveryManager.getInstance().getTaskLInfoList().get(String.valueOf(taskInfo.getTaskId()));
                if(null!=taskinfo && null!=mixNode){
                    taskinfo.getTaskInfo().setNodeId(mixNode.getNodeId());
                    mixNode.setNodeConsumption(mixNode.getNodeConsumption()+taskinfo.getTaskConsumption());
                }
            }
            taskInfo = ServiceDiscoveryManager.getInstance().getTaskQueue().poll();
        }
    }


    private  int  execMoveTask(int threshold){
            List<NodeInfo> nodeList  = ServiceDiscoveryManager.getInstance().getSortNodeinfoList();
            NodeInfo mixNode  = null;
            NodeInfo maxNode  = null;
            if(nodeList != null && nodeList.size() > 1){
                mixNode  = nodeList.get(0);
                int  mixVal = mixNode==null?0:mixNode.getNodeConsumption();
                maxNode  = nodeList.get(nodeList.size()-1);
                int  maxVal = maxNode==null?0:maxNode.getNodeConsumption();
                int taskval =  maxVal- mixVal;
                if (taskval > threshold){
                    //任务迁移

                }else{
                    return ReturnCodeKeys.E014;
                }
            }
        return  ReturnCodeKeys.E013;
    }

}
