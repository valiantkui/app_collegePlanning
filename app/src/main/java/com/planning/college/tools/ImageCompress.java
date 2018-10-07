package com.planning.college.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by KUIKUI on 2018-08-03.
 */

public class ImageCompress {




    /**

     * @param path

     * @return

     * @throws IOException

     * 压缩图片

     */

    public static Bitmap revitionImageSize(String path) throws IOException {

        //根据文件路径,创建一个字节缓冲输入流

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(

                new File(path)));

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        //根据流返回一个位图也就是bitmap，当options.inJustDecodeBounds = true的时候不需要完全解码，

        // 它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了

        BitmapFactory.decodeStream(in, null, options);

        //关闭流

        in.close();

        int i = 0;

        Bitmap bitmap = null;

        while (true) {

            // options.outWidth >> i 。右移运算符，num >> 1,相当于num除以2

            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {

                //得到一个输入流

                in = new BufferedInputStream(new FileInputStream(new File(path)));

                //为了解决图片解码时候出现SanpleSize错误，设置恰当的inSampleSize可以使BitmapFactory分配更少的空间以消除该错误

                //你将 inSampleSize 赋值为2,那就是每隔2行采1行,每隔2列采一列,那你解析出的图片就是原图大小的1/4.

                // Math.pow(2.0D, i)次方运算，2的i次方是多少

                options.inSampleSize = (int) Math.pow(2.0D, i);

                // 这里之前设置为了true，所以要改为false，否则就创建不出图片

                options.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeStream(in, null, options);

                break;

            }

            i += 1;

        }

        return bitmap;

    }



    /**

     * @param bitmap

     * 保存图片到SD卡的方法

     */

    public static void saveBitmapFile(String srcPath,String destPath){

        //Environment.getExternalStorageDirectory() 获取Android外部存储的空间，当有外部SD卡就在外部SD卡上建立。

        //没有外部SD卡就在内部SD卡的非data/data/目录建立目录。（data/data/目录才是真正的内存目录。）

        //IMAGE_NAME文件的名字，随便起。比如（xxx.jpg）

        Bitmap bitmap = null;
        try {
            bitmap = revitionImageSize(srcPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File tempFile = new File(destPath);

        try {

            //创建一个输出流，将数据写入到创建的文件对象中。

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));

            ////30 是压缩率，表示压缩70%; 如果不压缩是100，
            //此处发现 当压缩

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);


           /* 为什么要调用flush()方法？当FileOutputStream作为BufferedOutputStream构造函数的参数传入，然后对BufferedOutputStream进行写入操作，才能利用缓冲及flush()。

            查看BufferedOutputStream的源代码，发现所谓的buffer其实就是一个byte[]。

            BufferedOutputStream的每一次write其实是将内容写入byte[]，当buffer容量到达上限时，会触发真正的磁盘写入。

            而另一种触发磁盘写入的办法就是调用flush()了。*/

            bos.flush();

            //关闭流对象

            bos.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }




}
