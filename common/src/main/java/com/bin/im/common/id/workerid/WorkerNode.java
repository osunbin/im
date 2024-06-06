package com.bin.im.common.id.workerid;

import java.util.Date;

public class WorkerNode {

    /**
     * Entity unique id (table unique)
     */
    private long id;

    /**
     * Type of CONTAINER: HostName, ACTUAL : IP.
     */
    private String hostName;

    /**
     * Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)
     */
    private String port;


    /**
     * Worker launch date, default now
     */
    private Date launchDate = new Date();

    /**
     * Created time
     */
    private Date created;

    /**
     * Last modified
     */
    private Date modified;

    /**
     * Getters & Setters
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }



    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDateDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
