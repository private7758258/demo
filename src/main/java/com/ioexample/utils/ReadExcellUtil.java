package com.ioexample.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;

/**
 * @author hyf
 * @version 1.0
 * @description: TODO
 * @date 2024/4/24 9:39
 */
public class ReadExcellUtil {

    public static void ExcellRead(String fileUrl){
        try {
            FileInputStream in = new FileInputStream(fileUrl);
            Workbook wk = StreamingReader.builder()
                    //缓存到内存中的行数，默认是10
                    .rowCacheSize(100)
                    //读取资源时，缓存到内存的字节大小，默认是1024
                    .bufferSize(4096)
                    //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
                    .open(in);
            Sheet sheet = wk.getSheetAt(0);
            //遍历所有的行
            for (Row row : sheet) {
//                System.out.println("开始遍历第" + row.getRowNum() + "行数据：");
                //遍历所有的列
                for (Cell cell : row) {
//                    System.out.print(cell.getStringCellValue() + " ");
                }
                System.out.println(" ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
