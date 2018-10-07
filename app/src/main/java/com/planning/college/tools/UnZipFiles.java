package com.planning.college.tools;

import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by KUIKUI on 2018-07-25.
 */


public class UnZipFiles {
    public static void unZipFiles(String zipPath,String descDir)throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }
    /**
     * 解压文件到指定目录
     * @param zipFile
     * @param descDir  指定目录，
     * @author isea533
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFiles(File zipFile,String descDir)throws IOException{

        String indexPath = null;
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile
                zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
            ZipEntry
                    entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if(!file.exists()){
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if(new File(outPath).isDirectory()){
                continue;
            }
            //输出文件路径信息
            Log.i("path",outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0){
                out.write(buf1,0,len);
            }
            in.close();
            out.close();

            if (outPath.endsWith("index.html")){
                indexPath = outPath;
            }
        }
        //System.out.println("******************解压完毕********************");
        return indexPath;

    }

}
