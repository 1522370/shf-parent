package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-07  14:22
 */
public interface UserInfoService extends BaseService<UserInfo> {
    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    UserInfo getByPhone(String phone);

    /**
     * 根据昵称查询用户
     * @param nickname
     * @return
     */
    UserInfo getByNickname(String nickname);
}
