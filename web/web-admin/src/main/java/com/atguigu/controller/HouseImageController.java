package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-06  14:23
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    private static final String SHOW_ACTION = "redirect:/house/";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;
    private static final String PAGE_UPLOAD_SHOW = "house/upload";

    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type,
                             Model model){
        //将houseId和type存储到请求域
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",type);
        //显示图片上传页面
        return PAGE_UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") List<MultipartFile> fileList) throws IOException {
        //1. 遍历出上传的每一个文件
        if (!CollectionUtils.isEmpty(fileList)) {
            for (int i=0;i<fileList.size();i++) {
                MultipartFile multipartFile = fileList.get(i);
                //2. 将每一个文件上传到七牛云
                //2.1 上传的每一张图片都应该有唯一的文件名
                String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
                //2.2 创建三级目录结构
                String dateDirPath = FileUtil.getDateDirPath();

                String fileName = dateDirPath + uuidName;
                QiniuUtils.upload2Qiniu(multipartFile.getBytes(),fileName);
                //3. 将上传的图片的访问路径、上传的图片在七牛云中的路径、上传的图片对应的房源id保存到hse_house_image表中
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(fileName);
                //获取图片的访问路径
                String url = QiniuUtils.getUrl(fileName);
                houseImage.setImageUrl(url);
                houseImage.setType(type);
                houseImageService.insert(houseImage);

                //4. 判断房源是否有默认图片，如果没有，则可以将这次上传的第一张图片设置为当前房源的默认图片
                if (i == 0) {
                    //根据id获取房源信息
                    House house = houseService.getById(houseId);
                    //获取房源的默认图片路径
                    String defaultImageUrl = house.getDefaultImageUrl();
                    if (StringUtils.isEmpty(defaultImageUrl) || "null".equalsIgnoreCase(defaultImageUrl)) {
                        //当前房源没有默认图片
                        house.setDefaultImageUrl(url);
                        //更新房源信息
                        houseService.update(house);
                    }
                }
            }
        }
        return Result.ok();
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id){
        HouseImage houseImage = houseImageService.getById(id);
        //1. 从七牛云中删除这张图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //2. 从数据库表中删除图片
        houseImageService.delete(id);
        //3. 如果这张图片刚好是房源的默认图片,那么我们要修改房源的默认图片为空
        //3.1 查询房源信息
        House house = houseService.getById(houseId);
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //3.2 说明当前删除的这张图片刚好是房源的默认图片
            house.setDefaultImageUrl("null");
            houseService.update(house);
        }

        //重新显示房源的详情页
        return SHOW_ACTION + houseId;
    }
}
