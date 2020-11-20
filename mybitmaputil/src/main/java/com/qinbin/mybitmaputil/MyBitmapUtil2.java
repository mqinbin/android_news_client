package com.qinbin.mybitmaputil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 步骤
 * 1 开启子线程
 * 2 获取byte[]
 * 2.1 检查内存中是否有 ，有就到3，没有就到2.2
 * 2.2 检查磁盘中是否有 ，有就到2.4 ，没有 到2.3
 * 2.3 从网络加载   2.5
 * 2.4 写入内存   到3 去
 * 2.5 写入磁盘  到2.4
 * 3 获得bitmap对象
 * 4 回到主线程显示图片
 *
 *
 * 优化
 * 1 避免OOM内存
 *
 * 2 线程的创建
 *
 *
 * 知识
 *
 * LruCache
 * Least Recent Used
 * 用法和Map集合一样，但它有个好处，如果指定容量，会自动释放元素，不会超出容量限制
 *
 *  使用：
 *  1 构造时指定容量
 *  2 和map集合一样用 put  get
 *  3 重写sizeOf方法，可以规定元素的权重
 *
 *  Runtime
 */
public class MyBitmapUtil2 {
    private static Handler sHandler = new Handler();
    private final Context mContext;
    private File cacheDir;

    Runtime mRuntime = Runtime.getRuntime();
    {
        // java程序的内存 <- JVM 的内存 <- 进程的内存 <- 操作系统  <- 硬件
        // 已经分配的内存
        mRuntime.totalMemory();
        // 已经分配，但没有使用的内存， 可以避免频繁申请
        mRuntime.freeMemory();
        // 可以分配的最大内存
        mRuntime.maxMemory();
    }

    LruCache<String,byte[]> cache = new LruCache<String,byte[]>((int) (mRuntime.maxMemory() /8)){
        @Override
        protected int sizeOf(String key, byte[] value) {
            return value.length;
        }
    };

    //    private Map<String,byte[]> cache = new HashMap<>();
    public MyBitmapUtil2(Context context) {
        mContext = context;
        cacheDir = new File(Environment.getExternalStorageDirectory(), "/MBU/" + context.getPackageName());
        cacheDir.mkdirs();
    }

    ThreadPoolExecutor mThreadPoolExecutor =new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public void display(ImageView iv, String url) {
//        1 开启子线程
//        mImageView.setImageResource(R.mipmap.ic_launcher);
        // 线程的创建消耗资源的 ，
//        new Thread(new LoadImageRunnable(iv, url)).start();
        // 用线程池来代替创建线程
        mThreadPoolExecutor.execute(new LoadImageRunnable(iv, url));
    }

    public class LoadImageRunnable implements Runnable {
        ImageView mImageView;
        String url;

        public LoadImageRunnable(ImageView iv, String url) {
            this.mImageView = iv;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                //            2 获取byte[]
                byte[] bytes = getBytes(url);
//            3 获得bitmap对象
                Bitmap bitmap = getBitmap(bytes);
//            4 回到主线程显示图片
                sHandler.post(new ShowBmpRunnable(bitmap, mImageView));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private class ShowBmpRunnable implements Runnable {
        ImageView iv;
        Bitmap bitmap;

        public ShowBmpRunnable(Bitmap bitmap, ImageView iv) {
            this.bitmap = bitmap;
            this.iv = iv;
        }

        @Override
        public void run() {
            iv.setImageBitmap(bitmap);
        }
    }

    private byte[] getBytes(String url) throws IOException {
        byte[] result = null;

//        *  2.1 检查内存中是否有 ，有就到3，没有就到2.2
        result = readFromMem(url);
        if (result != null) {
            Log.d("getBytes" ,"内存");
            return result;
        }
//        *  2.2 检查磁盘中是否有 ，有就到2.4 ，没有 到2.3
        result = readFromDisk(url);
        if (result != null) {
            Log.d("getBytes" ,"磁盘");
            writeToMem(url, result);
            return result;
        }
//        *  2.3 从网络加载   2.5
//        *  2.5 写入磁盘  到2.4
//        *  2.4 写入内存   到3 去
        result = readFromNet(url);
        Log.d("getBytes" ,"网络 ");
        writeToDisk(url, result);
        writeToMem(url, result);
        return result;

    }

    private void writeToDisk(String url, byte[] result) throws IOException {
        // 写入文件
        // url --> file -> FileOutputStream -> 写入result
        File target = getFile(url);
        // java 7 新特性， 在try（）  括号中开的会自动关闭
        try (
                FileOutputStream fos = new FileOutputStream(target);
        ) {
            fos.write(result);
        }


    }


    private void writeToMem(String url, byte[] result) {

        cache.put(url,result);
    }

    private byte[] readFromNet(String url) throws IOException {
        // url -> InputStream  -> ByteArrayOutputStream -> byte[]
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            inputStream = new URL(url).openStream();
            baos = new ByteArrayOutputStream();
            copyStream(inputStream, baos);

        } finally {
            closeStream(inputStream);
            closeStream(baos);
//            inputStream.close();
//            baos.close();
        }

        return baos.toByteArray();
    }


    private byte[] readFromDisk(String url) throws IOException {
        //url -> File -> FileInputStream -> baos -> byte[]
        File file = getFile(url);
        if(!file.exists()){
            return null;
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ) {
            copyStream(fis, baos);
            return baos.toByteArray();
        }


    }

    private byte[] readFromMem(String url) {
        return cache.get(url);
    }

    private File getFile(String url) {
        // url --> file  要没有不安全的字符（/\"等） ； 一个url只能对应一个file， 两个url不能对应一个url，要唯一 ；同样的url的到同样的File
        // 文件 = 文件夹 + 文件名
        // 文件夹 在构造中定义了
        // 文件名用 md5

        return new File(cacheDir, md5(url));
    }

    private Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static void copyStream(InputStream is, OutputStream os) throws IOException {
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
    }

    //    private static void closeStream(InputStream is) {
//        if(is !=null){
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    private void closeStream(OutputStream os) {
//        if(os !=null){
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
