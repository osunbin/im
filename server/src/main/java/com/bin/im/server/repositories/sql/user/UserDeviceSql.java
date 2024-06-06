package com.bin.im.server.repositories.sql.user;

import com.bin.im.server.repositories.model.user.UserDevTokenModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDeviceSql {


    @SqlUpdate("insert into  t_user_dev_token   (uid,dev_type,token,version,timestamp) values(:uid,:devType,:token,:version,:timestamp) " +
            " ON DUPLICATE KEY UPDATE dev_type = :devType,token = :token,version = :version,timestamp = :timestamp ")
    void upsertUserDevice(@BindBean UserDevTokenModel userDevTokenModel);

}
