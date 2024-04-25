package com.ioexample.demo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version 1.0
 * @description: 读取csv文件
 * @date 2024/4/24 15:34
 */
public class CsvReadDemo {

    /**
      设置预订的列
     */
    static List<String> ALI_FINANCE_LIST = new ArrayList<>();
    /**
      读取的总数据
     */
    static Integer totalCount = 0;

    public static void main(String[] args) {
        ALI_FINANCE_LIST.add("电话");
        ALI_FINANCE_LIST.add("姓名");
        ALI_FINANCE_LIST.add("测试1");
        ALI_FINANCE_LIST.add("编号");
        ALI_FINANCE_LIST.add("班级");
        ALI_FINANCE_LIST.add("学校");
        ALI_FINANCE_LIST.add("学校1");
        ALI_FINANCE_LIST.add("学校2");
        ALI_FINANCE_LIST.add("学校3");
        ALI_FINANCE_LIST.add("学校4");
        ALI_FINANCE_LIST.add("学校5");
        File file = new File("D:\\Utils\\defaults\\测试数据\\cs");

        // 开始时间
        long stime = System.currentTimeMillis();
        //可以按照需求自行排序
        File[] childs = file.listFiles();
        for (File file1 : childs) {
            if (!file1.getName().contains(".csv") && file1.getName().contains("zhs")) {
                file1.renameTo(new File(file1.getAbsolutePath() + ".csv"));
            }
        }
        int batchNumOrder = 1;
        List<Map<String, Object>> context = new ArrayList<>();
        for (File child : childs) {
            if (!child.getName().contains("zhs")){
                continue;
            }
            batchNumOrder = parseFile(child, batchNumOrder, context, 500);
        }

        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.println(totalCount.toString());
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }

    private static int parseFile(File file, int batchNumOrder, List<Map<String, Object>> context, int count) {
        try (
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)
        ) {
            batchNumOrder = readDataFromFile(br, context, batchNumOrder, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batchNumOrder;
    }

    private static int readDataFromFile(BufferedReader br, List<Map<String, Object>> context, int batchNumOrder, int count) throws IOException {
        String line = "";
        int number = 1;
        while ((line = br.readLine()) != null) {
            //去除#号开始的行
            if (!line.startsWith("#")) {
                if (number >= 1) {
                    //csv是以逗号为区分的文件，以逗号区分
                    String[] columns = line.split(",", -1);
                    //构建数据
                    context.add(constructDataMap(columns));
                }
                number++;
            }

            if (context.size() >= count) {
                // TODO 存表
                System.out.println("=====插入数据库：批次:" + batchNumOrder + ",数据条数:" + context.size());
                totalCount = totalCount + context.size();
                context.clear();
                batchNumOrder++;
            }
        }
        // 最后一批次提交
        if (CollectionUtils.isNotEmpty(context)) {
            System.out.println("=====插入数据库：批次:" + batchNumOrder + ",数据条数:" + context.size());
            totalCount = totalCount + context.size();
            context.clear();
        }
        return batchNumOrder;
    }


    public static Map<String, Object> constructDataMap(String[] columns) {
        Map<String, Object> dataMap = new HashMap<>(16);
        for (int i = 0; i < columns.length; i++) {
            //防止异常，大于预定义的列不处理
            if (i > ALI_FINANCE_LIST.size()) {
                break;
            }
            try {
                dataMap.put(ALI_FINANCE_LIST.get(i), columns[i].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataMap;
    }
}
