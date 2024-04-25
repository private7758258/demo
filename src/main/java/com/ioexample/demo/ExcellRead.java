package com.ioexample.demo;

import com.ioexample.utils.ReadExcellUtil;

/**
 * @author hyf
 * @version 1.0
 * @description: 读取Excell
 * @date 2024/4/23 14:52
 */
public class ExcellRead {
    public static void main(String[] args) {
        // 开始时间
        long stime = System.currentTimeMillis();
//        ReadExcellUtil.ExcellRead("D:\\Utils\\defaults\\测试数据\\户籍\\户籍\\1 - 副本 (2).xlsx");
        ReadExcellUtil.ExcellRead("D:\\Utils\\defaults\\测试数据\\zhs\\zhs.csv");
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }

}
