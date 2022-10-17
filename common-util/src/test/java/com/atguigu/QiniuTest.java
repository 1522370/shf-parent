package com.atguigu;

import com.atguigu.util.QiniuUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 包名:com.atguigu
 *
 * @author Leevi
 * 日期2022-10-06  14:10
 */
public class QiniuTest {
    @Test
    public void testUpload2Qiniu(){
        QiniuUtils.upload2Qiniu("D:\\qiniu\\test.jpg","aa/bb/cc/abc.jpg");
    }

    @Test
    public void testUpload2QiniuAnother() throws IOException {
        //1. 将"D:\qiniu\test.jpg"转成字节输入流
        InputStream is = new FileInputStream("D:\\qiniu\\test.jpg");
        //2. 创建字节数组
        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        QiniuUtils.upload2Qiniu(buffer,"aa/bb/test.jpg");
        is.close();
    }

    @Test
    public void testDelete(){
        QiniuUtils.deleteFileFromQiniu("aa/bb/test.jpg");
        QiniuUtils.deleteFileFromQiniu("aa/bb/cc/abc.jpg");
    }

    @Test
    public void testGetUrl(){
        String url = QiniuUtils.getUrl("d61bd0db-9b94-4199-85e1-8360606f9c99.jpg.480x640.jpg.55x55.jpg");
        System.out.println(url);
    }
}
