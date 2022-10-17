package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-07  14:37
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfo getByPhone(String phone);

    UserInfo getByNickname(String nickname);
}
