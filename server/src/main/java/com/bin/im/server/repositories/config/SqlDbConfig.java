package com.bin.im.server.repositories.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

public class SqlDbConfig {


    public static HikariDataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(getJdbcUrl());
        ds.setDriverClassName("org.h2.Driver");
        ds.setUsername("im");
        ds.setPassword((String) null);
        ds.setMinimumIdle(1);
        ds.setMaximumPoolSize(10);
        return ds;
    }

    public static DataSource multipleDataSource() {
        DataSource dataSource = null;
        try {
            dataSource = ShardingSphereDataSourceFactory.
                    createDataSource(new HashMap<>(),
                            Collections.EMPTY_LIST,
                            new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    public static Jdbi jdbi;

    static {
        jdbi = jdbi();
    }

    public static Jdbi jdbi() {

        Jdbi jdbi = Jdbi.create(dataSource());



        jdbi.installPlugin(new H2DatabasePlugin());
        return jdbi;
    }


    private static String getJdbcUrl() {
        String dbPath = Paths.get(System.getProperty("user.dir")).resolve("sql").resolve("im").toString().replace("\\", "/");
        return "jdbc:h2:file:" + dbPath + ";USER=" + "im" + ";MODE=MYSQL";
    }


}
