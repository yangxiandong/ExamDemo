package com.migu.schedule.manager;
import com.migu.schedule.constants.SystemConstants;
import com.migu.schedule.info.NodeInfo;
import com.migu.schedule.info.TaskInfo;
import com.migu.schedule.info.TaskRunInfo;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceDiscoveryManager {

    public final static ServiceDiscoveryManager INSTANCE = new ServiceDiscoveryManager();

    Queue<TaskInfo> taskQueue = null;

    //物理机器编号和对应的消耗
    protected   ConcurrentHashMap<String, TaskRunInfo> taskLInfoList = null;

    //物理机器编号和对应的消耗
    protected List<NodeInfo> nodeinfoList = null;
   ;

    private ServiceDiscoveryManager() {
    }

    public static ServiceDiscoveryManager getInstance() {
        return INSTANCE;
    }

    public void  rebuild() {
        taskQueue =  new ArrayBlockingQueue(SystemConstants.TASK_QUEUE_INIT_SIZE);
        taskLInfoList = new ConcurrentHashMap<String, TaskRunInfo>();
        nodeinfoList = new ArrayList<NodeInfo>();
    }

    public Queue<TaskInfo> getTaskQueue() {
        return taskQueue;
    }


    public void  addTask(TaskInfo taskInfo,int taskConsumption) {
          if(null == taskInfo){
              return ;
          }
        if(taskQueue.add(taskInfo)){
            taskLInfoList.put(String.valueOf(taskInfo.getTaskId()),new TaskRunInfo(taskInfo,taskConsumption));
        }
    }


    public List<NodeInfo> getSortNodeinfoList() {
        Collections.sort(nodeinfoList,new Comparator<NodeInfo>() {
            public int compare(NodeInfo o1, NodeInfo o2) {
                 int  n = o1.getNodeConsumption() - o2.getNodeConsumption();
                 if(n == 0){
                     n = o1.getNodeId() - o2.getNodeId();
                 }
                return n;
            }
        });
        return  nodeinfoList;
    }

    public List<NodeInfo> getNodeinfoList() {
        return  nodeinfoList;
    }

    public NodeInfo getNodeinfo(int  nodeId) {
           for(NodeInfo nodeInfo: nodeinfoList){
               if(nodeInfo.getNodeId()==nodeId) return nodeInfo;
           }
        return  null;
    }

    public void  removeNodeInfo(NodeInfo nodeInfo) {
            if(nodeinfoList != null && nodeInfo != null){
                nodeinfoList.remove(nodeInfo) ;
            }
    }
    public void  addNodeInfo(NodeInfo nodeInfo) {
        if(nodeinfoList != null && nodeInfo != null){
            nodeinfoList.add(nodeInfo) ;
        }
    }

    public ConcurrentHashMap<String, TaskRunInfo> getTaskLInfoList() {
        return taskLInfoList;
    }
}
