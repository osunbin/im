package com.bin.im.common.id.workerid;

public class WorkerNodeDAO {
    


    public void addWorkerNode(WorkerNode workerNode) {
        String sql = "INSERT INTO WORKER_NODE(HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED) " + " VALUES (?, ?, ?, ?, NOW(), NOW())";
    }
    
}
