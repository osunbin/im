package com.bin.im.server.repositories.sql.user;

import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.user.UserDevTokenModel;
import com.bin.im.server.repositories.model.user.UserLoginModel;
import com.bin.im.server.repositories.sql.BaseDao;

public class UserDao extends BaseDao {



    public void upsertUserDeviceId(UserDevTokenModel userDevToken) {
        getJdbi().useExtension(UserDeviceSql.class, dao -> dao.upsertUserDevice(userDevToken));
    }

    public void upsertLoginBitMap(UserLoginBitMap userLoginBitMap) {
        getJdbi().useExtension(UserLoginBitMapSql.class, dao -> dao.upsertLoginData(userLoginBitMap));
    }

    public DasResultModel<UserLoginBitMap> queryLoginBitMap(long uid) {
        UserLoginBitMap userLoginBitMap = getJdbi().withExtension(UserLoginBitMapSql.class, dao -> dao.selectLoginBitMap(uid));
        return DasResultModel.ofOk(userLoginBitMap);
    }

    public void upsertLoginData(UserLoginModel userLogin) {
        getJdbi().useExtension(UserLoginSql.class, dao -> dao.upsertLoginData(userLogin));
    }

    public void upsertLogoutData(UserLoginModel userLogin) {
        getJdbi().useExtension(UserLoginSql.class, dao -> dao.updateLogoutTime(userLogin));
    }

}
