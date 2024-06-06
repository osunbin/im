package com.bin.im.common.id.workerid;

import com.bin.im.common.internal.utils.IPConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * DB编号分配器(利用数据库来管理)
 *
 * @author yutianbao
 */
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableWorkerIdAssigner.class);


    private WorkerNodeDAO workerNodeDAO;


    public DisposableWorkerIdAssigner(WorkerNodeDAO workerNodeDAO) {
        this.workerNodeDAO = workerNodeDAO;
    }

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Override
    public long assignWorkerId() {
        // build worker node entity
        WorkerNode workerNodeEntity = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        workerNodeDAO.addWorkerNode(workerNodeEntity);
        LOGGER.info("Add worker node:" + workerNodeEntity);

        return workerNodeEntity.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNode buildWorkerNode() {
        WorkerNode workerNodeEntity = new WorkerNode();
        workerNodeEntity.setHostName(IPConverter.getIP());
        workerNodeEntity.setPort(System.currentTimeMillis() + "-" + new Random().nextInt(100000));
        return workerNodeEntity;
    }

}
