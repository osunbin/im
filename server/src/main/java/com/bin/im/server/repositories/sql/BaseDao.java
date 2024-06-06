package com.bin.im.server.repositories.sql;


import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BaseDao {

    protected  final Logger logger = LoggerFactory.getLogger(getClass());


    private Jdbi jdbi;


    public void setJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public BaseDao() {

    }

    private  String table;

    public BaseDao(String table) {
        this.table = table;
    }

    protected Query buildQuery(Handle handle, String sql, long uid) {
        return handle.createQuery(sql)
                .define("table", getTable(uid));
    }

    protected Update buildUpdate(Handle handle, String sql, long uid) {
        return handle.createUpdate(sql)
                .define("table", getTable(uid));
    }

    protected Jdbi getConnect(long uid) {
        return null;
    }

    private String getTable(long uid) {
        return "";
    }


}
