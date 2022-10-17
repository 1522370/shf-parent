package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.AtguiguConstant;
import com.atguigu.en.YesOrNo;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.LoginVo;
import com.atguigu.entity.vo.RegisterVo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-07  14:13
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone, HttpSession session){
        //1. 调用阿里云短信的API,给phone发送一个随机验证码
        String code = "1111";
        //2. 将发送给phone的验证码存储到redis,并且设置时效性; 或者存储到session
        session.setAttribute("CODE",code);
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpSession session){
        //1. 校验验证码是否正确
        //1.1 获取用户输入的验证码
        String code = registerVo.getCode();
        //1.2 获取服务器保存的验证码
        String sessionCode = (String) session.getAttribute("CODE");
        if (!code.equalsIgnoreCase(sessionCode)) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //2. 校验手机号是否被占用:调用业务层的方法根据手机号查询用户,如果能查询到说明占用了
        String phone = registerVo.getPhone();
        if (!ObjectUtils.isEmpty(userInfoService.getByPhone(phone))) {
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //3. 校验昵称是否被占用:调用业务层的方法根据昵称查询用户,如果能查询到说明占用了
        String nickName = registerVo.getNickName();
        if (!ObjectUtils.isEmpty(userInfoService.getByNickname(nickName))) {
            return Result.build(null,ResultCodeEnum.NICKNAME_REGISTER_ERROR);
        }
        //4. 将密码进行加密
        registerVo.setPassword(MD5.encrypt(registerVo.getPassword()));
        //5. 将数据保存到数据库
        UserInfo userInfo = new UserInfo();
        //属性对拷
        BeanUtils.copyProperties(registerVo,userInfo);
        //设置status:表示账号是否被锁定
        userInfo.setStatus(YesOrNo.YES.getCode());
        userInfoService.insert(userInfo);

        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpSession session){
        //1. 校验手机号是否正确
        String phone = loginVo.getPhone();
        //根据手机号查询用户
        UserInfo userInfo = userInfoService.getByPhone(phone);
        if (ObjectUtils.isEmpty(userInfo)) {
            //如果查询不到，说明手机号错误
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        //2. 校验密码是否正确
        //获取用户输入的密码
        String password = loginVo.getPassword();
        //获取数据库中的密码
        String dbPassword = userInfo.getPassword();
        if (!dbPassword.equals(MD5.encrypt(password))) {
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //3. 校验账号是否被锁定
        if (Objects.equals(userInfo.getStatus(), YesOrNo.NO.getCode())) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //4. 登录成功，则将登录的用户保存到session中
        session.setAttribute(AtguiguConstant.SessionConstant.USER_KEY,userInfo);

        //5. 将用户nickName以及phone响应给客户端
        Map resultMap = new HashMap<>();
        resultMap.put("nickName",userInfo.getNickName());
        resultMap.put("phone",userInfo.getPhone());

        return Result.ok(resultMap);
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session){
        session.invalidate();

        return Result.ok();
    }
}
