package com.ioexample.config;

import com.ioexample.utils.ReadExcellUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zfh on 16-4-19.
 */
public class MappedBiggerFileReader {
    private MappedByteBuffer[] mappedBufArray;
    private int count = 0;
    private int number;
    private FileInputStream fileIn;
    private long fileLength;
    private int arraySize;
    private byte[] array;

    public MappedBiggerFileReader(String fileName, int arraySize) throws IOException {
        this.fileIn = new FileInputStream(fileName);
        FileChannel fileChannel = fileIn.getChannel();
        this.fileLength = fileChannel.size();
        this.number = (int) Math.ceil((double) fileLength / (double) Integer.MAX_VALUE);
        // 内存文件映射数组
        this.mappedBufArray = new MappedByteBuffer[number];
        long preLength = 0;
        // 映射区域的大小
        long regionSize = (long) Integer.MAX_VALUE;
        // 将文件的连续区域映射到内存文件映射数组中
        for (int i = 0; i < number; i++) {
            if (fileLength - preLength < (long) Integer.MAX_VALUE) {
                // 最后一片区域的大小
                regionSize = fileLength - preLength;
            }
            mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
            // 下一片区域的开始
            preLength += regionSize;
        }
        this.arraySize = arraySize;
    }

    public int read() throws IOException {
        if (count >= number) {
            return -1;
        }
        int limit = mappedBufArray[count].limit();
        int position = mappedBufArray[count].position();
        if (limit - position > arraySize) {
            array = new byte[arraySize];
            mappedBufArray[count].get(array);
            return arraySize;
        } else {// 本内存文件映射最后一次读取数据
            array = new byte[limit - position];
            mappedBufArray[count].get(array);
            if (count < number) {
                count++;// 转换到下一个内存文件映射
            }
            return limit - position;
        }
    }

    public void close() throws IOException {
        fileIn.close();
        array = null;
    }

    public byte[] getArray() {
        return array;
    }

    public long getFileLength() {
        return fileLength;
    }

    public static void main(String[] args) throws IOException {
        MappedBiggerFileReader reader = new MappedBiggerFileReader("D:\\Utils\\defaults\\测试数据\\zhs\\zhs.csv", 65536);
        long start = System.nanoTime();
        while (reader.read() != -1) ;
        long end = System.nanoTime();
        reader.close();
        System.out.println("MappedBiggerFileReader: " + (end - start));
    }
}