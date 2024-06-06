package com.bin.im.server.common.type;

import com.bin.im.common.internal.utils.StringUtils;

public class UserType {

    public static final int USER_SELLER = 0, //卖家
            USER_BUYER = 1, //买家
            USER_NORMAL = 2; // 普通


    private int getUidType(String userType) {
        int type = 0;
        if (StringUtils.isNotEmpty(userType)) {
            switch (userType) {
                case "n": {type = 0; break;} // 普通
                case "m": {type = 1; break;} // 母号
                case "c": {type = 2; break;} // 子号
            }
        }
        return type;
    }
}
