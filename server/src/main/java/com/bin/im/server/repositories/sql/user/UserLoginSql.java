package com.bin.im.server.repositories.sql.user;

import com.bin.im.server.repositories.model.user.UserLoginModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserLoginSql {


    @SqlUpdate("update t_user_login set logout_time = :logoutTime  where uid = :uid and source_type = :sourceType")
    void updateLogoutTime(@BindBean UserLoginModel userLogin);

    @SqlUpdate("insert into t_user_login (uid,login_time,logout_time,ip,source_type) values(:uid,:loginTime,:logoutTime,:ip,:sourceType) " +
            " ON DUPLICATE KEY UPDATE  login_time = :loginTime,logout_time=:logoutTime,ip=:ip")
    void upsertLoginData(@BindBean UserLoginModel userLogin);
}
