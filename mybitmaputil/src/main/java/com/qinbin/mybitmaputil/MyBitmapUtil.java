package com.qinbin.mybitmaputil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
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
 * <p/>
 * <p/>
 * 优化
 * 性能优化
 * 1 避免OOM内存
 * <p/>
 * 2 线程的创建
 *
 * 3 大图片的优化
 * TODO
 *
 * <p/>
 * 写法的优化
 * 1 使用AsyncTask来代替
 * <p/>
 * 知识
 * <p/>
 * LruCache
 * Least Recent Used
 * 用法和Map集合一样，但它有个好处，如果指定容量，会自动释放元素，不会超出容量限制
 * <p/>
 * 使用：
 * 1 构造时指定容量
 * 2 和map集合一样用 put  get
 * 3 重写sizeOf方法，可以规定元素的权重
 * <p/>
 * Runtime
 * <p/>
 * <p/>
 * Pair
 * public class Family{
 * Man man;
 * Woman woman;
 * }
 * Family f = new Family(man,woman)
 * Man man = f.man;
 * <p/>
 * Pair<Man,Woman>
 * Pair<Man,Woman>  p = new Pair(man,woman);
 * Man man = p.first ;
 *
 * AsyncTask的优缺点：
 * 优点：
 * 在主线程和子线程切换非常的方便
 * 缺点：围绕着 线程池的构造
 * 1 不管新版旧版 都是128+10左右的 任务， 如果给定了超过词数量的任务，会出错
 * 2 在旧版（< API 19）的代码中， 最大线程数量为128, 那么 cpu的性能会消耗在线程切换上，所以新版的是按照cpu核心数量走
 * 3 不能按照优先级高低进行执行，优先级低的迟迟没有执行完，可能导致优先级高的任务在队列中等待
 *
 * 解决
 * 使用executeOnExecutor 来指定执行器 执行
 * 1 可以指定一个队列非常大的执行器
 * 2 可以模仿新版指定一个执行器
 * 3 可以不同优先级的任务指定单独的执行器
 *
 * 注意
 * new出了一个Task，只能执行一次任务，因为它内部有状态， 等待执行、执行中 ，执行完成了
 *
 *
 *  bug 解决
 *  图片乱跳
 *  原因 复用了ImageView，display方法是异步的
 *
 * 对我们使用图片类库的启示：
 * 1 要使用单例，避免有多份内存缓存
 * 2 尽可能使用一个图片类库，而不是使用多个
 * 3 要进行图片缩放
 */
public class MyBitmapUtil {
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

    LruCache<String, byte[]> cache = new LruCache<String, byte[]>((int) (mRuntime.maxMemory() / 8)) {
        @Override
        protected int sizeOf(String key, byte[] value) {
            return value.length;
        }
    };

    //    private Map<String,byte[]> cache = new HashMap<>();
    public MyBitmapUtil(Context context) {
        mContext = context;
        cacheDir = new File(Environment.getExternalStorageDirectory(), "/MBU/" + context.getPackageName());
        cacheDir.mkdirs();
    }

    ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public void display(ImageView iv, String url) {
//        1 开启子线程
//        mImageView.setImageResource(R.mipmap.ic_launcher);
        // 线程的创建消耗资源的 ，
//        new Thread(new LoadImageRunnable(iv, url)).start();
        // 用线程池来代替创建线程
//        mThreadPoolExecutor.execute(new LoadImageRunnable(iv, url));

        // 显示空白 或者是占位图片，避免显示错误的图片
        iv.setImageBitmap(null);
        // 做标记
        iv.setTag(url);

        new LoadImageTask().execute(Pair.create(url, iv));
    }



    private byte[] getBytes(String url) throws IOException {
        byte[] result = null;

//        *  2.1 检查内存中是否有 ，有就到3，没有就到2.2
        result = readFromMem(url);
        if (result != null) {
            Log.d("getBytes", "内存");
            return result;
        }
//        *  2.2 检查磁盘中是否有 ，有就到2.4 ，没有 到2.3
        result = readFromDisk(url);
        if (result != null) {
            Log.d("getBytes", "磁盘");
            writeToMem(url, result);
            return result;
        }
//        *  2.3 从网络加载   2.5
//        *  2.5 写入磁盘  到2.4
//        *  2.4 写入内存   到3 去
        result = readFromNet(url);
        Log.d("getBytes", "网络 ");
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

        cache.put(url, result);
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
        if (!file.exists()) {
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

    private class LoadImageTask extends AsyncTask<Pair<String, ImageView>, Void, Pair<Bitmap, Pair<String, ImageView>>> {
        @Override
        protected Pair<Bitmap, Pair<String, ImageView>> doInBackground(Pair<String, ImageView>... params) {
            Pair<String, ImageView> pair = params[0];
            String url = pair.first;
            ImageView imageView = pair.second;
            try {
                //            2 获取byte[]
                byte[] bytes = getBytes(url);
//            3 获得bitmap对象
                Bitmap bitmap = getBitmap(bytes);
//            4 回到主线程显示图片
//                sHandler.post(new ShowBmpRunnable(bitmap, imageView));
                return new Pair<>(bitmap, pair);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Pair<Bitmap, Pair<String, ImageView>> result) {
            super.onPostExecute(result);
            if(result.second.first.equals(result.second.second.getTag())){
              result.second.second.setImageBitmap(result.first);
            }
        }
    }
}
