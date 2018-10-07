package com.planning.college.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by KUIKUI on 2018-08-04.
 */

public class IOUtil {

    /**
     * 通过指定路径和文件名来获取文件对象，当文件不存在时自动创建
     * @param path
     * @return
     * @throws IOException
     */
    public static File getFile(String path) throws IOException {
        // 创建文件对象
        File file;
       if(path == null || "".equals(path)){
           throw new FileNotFoundException("路径为空异常");
       }


       file = new File(path);
       if(!file.getParentFile().exists()){
           file.getParentFile().mkdirs();
       }

       if(!file.exists()){
           file.createNewFile();
       }


        // 返回文件
        return file;
    }

    /**
     * 获得指定文件的输出流
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getFileStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    /**
     * 将多个文件压缩
     * @param fileList 待压缩的文件列表
     * @param zipFilePath 压缩文件路径
     * @return 返回压缩好的文件
     * @throws IOException
     */
    public static File getZipFile(List<File> fileList, String zipFilePath) throws IOException {
        File zipFile = getFile(zipFilePath);
        // 文件输出流
        FileOutputStream outputStream = getFileStream(zipFile);
        // 压缩流
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        int size = fileList.size();
        // 压缩列表中的文件
        for (int i = 0;i < size;i++) {
            File file = fileList.get(i);
            zipFile(file, zipOutputStream);
        }
        // 关闭压缩流、文件流
        zipOutputStream.close();
        outputStream.close();
        return zipFile;
    }

    /**
     * 将文件数据写入文件压缩流
     * @param file 带压缩文件
     * @param zipOutputStream 压缩文件流
     * @throws IOException
     */
    private static void zipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ZipEntry entry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(entry);

                final int MAX_BYTE = 10 * 1024 * 1024; // 最大流为10MB
                long streamTotal = 0; // 接收流的容量
                int streamNum = 0; // 需要分开的流数目
                int leaveByte = 0; // 文件剩下的字符数
                byte[] buffer; // byte数据接受文件的数据

                streamTotal = bis.available(); // 获取流的最大字符数
                streamNum = (int) Math.floor(streamTotal / MAX_BYTE);
                leaveByte = (int) (streamTotal % MAX_BYTE);

                if (streamNum > 0) {
                    for (int i = 0;i < streamNum;i++) {
                        buffer = new byte[MAX_BYTE];
                        bis.read(buffer, 0, MAX_BYTE);
                        zipOutputStream.write(buffer, 0, MAX_BYTE);
                    }
                }

                // 写入剩下的流数据
                buffer = new byte[leaveByte];
                bis.read(buffer, 0, leaveByte); // 读入流
                zipOutputStream.write(buffer, 0, leaveByte); // 写入流
                zipOutputStream.closeEntry(); // 关闭当前的zip entry

                // 关闭输入流
                bis.close();
                fis.close();
            }
        }
    }
}

