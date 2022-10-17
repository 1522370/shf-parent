package com.atguigu.base;

import org.springframework.ui.Model;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-09-30  15:22
 */
public class BaseController {
    private static final String PAGE_SUCCESS = "common/successPage";
    public String successPage(Model model,String messagePage){
        //2. 将成功信息保存到请求域
        model.addAttribute("messagePage",messagePage);
        //3. 返回successPage.html页面的逻辑视图
        return PAGE_SUCCESS;
    }
}
