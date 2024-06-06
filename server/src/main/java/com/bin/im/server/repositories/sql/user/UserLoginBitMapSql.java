package com.bin.im.server.repositories.sql.user;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserLoginBitMapSql {


    @SqlQuery("select login_bmp loginBitMap,login_day loginDay from t_user_login_bmp where uid = :uid")
    UserLoginBitMap selectLoginBitMap(@Bind("uid") long uid);

    @SqlUpdate(" insert into  t_user_login_bmp (uid,login_bmp,login_day)  values(:uid,:loginBitMap,:loginDay) " +
            "  ON DUPLICATE KEY UPDATE  login_bmp = :loginBitMap, login_day = :loginDay")
    void upsertLoginData(@BindBean UserLoginBitMap userLoginBitMap);
}
