package com.example.printer319;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import printbigIMG.zpBigImg;
import zpSDK.zpSDK.zpBluetoothPrinter;

/**
 * create by 大白菜
 * description
 */
public class PrinterUtil {

    private static PrinterUtil mPrintUtil;
    private boolean isConnect = false;
    private static Context mContext;
    private String mac;
    private MessageBack messageBack;
    private ConnectThread thread;
    private MyProgressDialog DIALOG;




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isConnect) {
                        DIALOG.failed("发送失败，请重试！");
                        return;
                    }
                    if (!HOLDER.PRINTER.connect(mac)) {
                        DIALOG.failed("连接失败，请重试！");
                        return;
                    }
                    mHandler.sendEmptyMessageDelayed(1, 100L);
                    break;
                case 1:
//                    HOLDER.PRINTER.pageSetup(480, 300);
//                    HOLDER.PRINTER.drawText(10, 10, "豆浆:01-03 12:55", 12, 0, 1, false, false);
//                    HOLDER.PRINTER.drawText(10, 50, "存储时间:01-03 12:55", 2, 0, 0, false, false);
//                    HOLDER.PRINTER.drawText(10, 90, "到期时间:01-03 12:55", 2, 0, 0, false, false);
//                    HOLDER.PRINTER.drawText(10, 130, "可用时间:01-03 12:55", 2, 0, 0, false, false);
//                    HOLDER.PRINTER.drawText(10, 170, "菜库 补打", 2, 0, 0, false, false);
//                    HOLDER.PRINTER.drawText(10, 250, "00037", 2, 0, 0, false, false);
//                    HOLDER.PRINTER.drawText(150, 170, "5", 10, 0, 1, false, false);
//                    HOLDER.PRINTER.drawQrCode(230, 50, "产品名称： 豆浆\n" +
//                            "产品编码：TEST_OPSMATERIAL_CODE7\n" +
//                            "批次号：2020-01-03 15：55：00\n" +
//                            "到期时间：2020-01-03 18：55：00\n" +
//                            "可用时间：2020-01-03 18：55：00\n" +
//                            "标签：37", 0, 3, 2);
//                    HOLDER.PRINTER.print(0, 1);
                    HOLDER.PRINTER.drawGraphic(10, 0, 600, 338, getViewBitmap((Activity) mContext/*, categoryBean*/, 0, true));
//                    HOLDER.PRINTER.Write(new byte[]{0x1d,0x0c});
                    mHandler.sendEmptyMessageAtTime(2, 500L);
                    break;
                case 2:
                    //更新进度条
                    HOLDER.PRINTER.disconnect();
                    DIALOG.cancel();
//                    DIALOG.success("发送成功");
                    isConnect = false;
                    if (thread != null) {
                        thread.interrupt();
                        thread = null;
                    }
                    break;
                case 3:
                    boolean flag = (boolean) msg.obj;
                    messageBack.connect(flag);
                    if (!flag)
                        DIALOG.failed("连接失败，请重试！");
                    break;
                case 4:
                    DIALOG.failed("打印机忙，请重试！");
                    break;
                default:
            }
        }
    };

    public static PrinterUtil getInstance(Context context) {
        mContext = context;
        if (mPrintUtil == null) {
            Log.v("zgy","null");
            synchronized (PrinterUtil.class) {
                if (mPrintUtil == null) {
                    mPrintUtil = new PrinterUtil();
                }
            }
        }
        Log.v("zgy","not null");
        return mPrintUtil;
    }

    private PrinterUtil() {

    }

    private static final class HOLDER {
        //        public static final zpBluetoothPrinter PRINTER = new zpBluetoothPrinter(mContext);
        public static final zpBigImg PRINTER = new zpBigImg(mContext);
//        public static final MyProgressDialog DIALOG = new MyProgressDialog(mContext, R.style.CustomDialog, "正在发送数据...");
    }

    public void connect(String address, MessageBack messageBack) {
        DIALOG = new MyProgressDialog(mContext, R.style.CustomDialog, "正在发送数据...");
        mac = address;
        DIALOG.show();
        this.messageBack = messageBack;
        if (isConnect) {
            mHandler.sendEmptyMessage(4);
            return;
        }
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        thread = new ConnectThread();
        thread.start();
    }
    class CategoryBean {
        private String categoryName;
        private String batch;
    }


    public void print(CategoryBean bean) {

        mHandler.sendEmptyMessageDelayed(1, 50L);
    }

    class ConnectThread extends Thread {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = 3;
            if (HOLDER.PRINTER.connect(mac)) {
                message.obj = true;
            } else {
                message.obj = false;
            }
            mHandler.sendMessage(message);
        }
    }

    interface MessageBack {
        void connect(boolean iRet);
    }

    public static Bitmap getViewBitmap(Activity activity/*, CategoryBean categoryBean*/, int repeatCode, boolean isReplenish) {

        String str2 = repeatCode + "";
        String str3 = "";
        if (str2.length() < 5) {
            for (int j = 0; j < 5 - str2.length(); j++) {
                str3 = str3 + "0";
            }
            str2 = str3 + str2;
        }
        //将布局转化成view对象
        View viewBitmap = LayoutInflater.from(activity).inflate(R.layout.print_view, null);

        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        viewBitmap.layout(0, 0, width, height);
        int measuredWidth = 600;
        int measuredHeight = 600;

        viewBitmap.measure(measuredWidth, measuredHeight);
        viewBitmap.layout(0, 0, viewBitmap.getMeasuredWidth(), viewBitmap.getMeasuredHeight());

        viewBitmap.setDrawingCacheEnabled(true);
        viewBitmap.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        viewBitmap.setDrawingCacheBackgroundColor(Color.WHITE);

        ImageView imageView = viewBitmap.findViewById(R.id.print_img_1);
        TextView tvName = viewBitmap.findViewById(R.id.print_1);
        TextView tvbatch = viewBitmap.findViewById(R.id.print_txt_1);
        TextView tvExpiredTime = viewBitmap.findViewById(R.id.print_txt_3);
        TextView tvVegetables = viewBitmap.findViewById(R.id.print_5);
        TextView tvWeek = viewBitmap.findViewById(R.id.print_6);
        TextView tvAvailable = viewBitmap.findViewById(R.id.print_txt_2);
        TextView textView = viewBitmap.findViewById(R.id.print_7);
        TextView tvRepeatCode = viewBitmap.findViewById(R.id.print_8);

        tvName.setText("商品");
        tvbatch.setText("aaaaa");
        tvExpiredTime.setText("ssssss");
        tvWeek.setText("ssssssss");
        tvRepeatCode.setText(str2);
        if (isReplenish) {
            tvVegetables.setVisibility(View.VISIBLE);
        } else {
            tvVegetables.setVisibility(View.INVISIBLE);
        }
        String available = "";
        if (!TextUtils.isEmpty("ssss") ){
            tvAvailable.setText("swewew");
            tvAvailable.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            available = "\n可用时间:" + "dsdsdsds";
        } else {
            tvAvailable.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }


        String text = "产品名称:" + "sdd" + "\n产品编码:" + "dsdssdsd"
                + "\n批次号:" +"22222" + "\n到期时间:" + "3233242" + available + "\n标签:" + repeatCode;
        Log.e("PictureUtil", "getViewBitmap: 打印字符串" + text + "   ");
        imageView.setImageBitmap(createQRCodeBitmap(text, 200, 200, "UTF-8", "M", "0", Color.BLACK, Color.WHITE));
        // 把一个View转换成图片
        Bitmap cachebmp = viewConversionBitmap(viewBitmap);
        return cachebmp;
    }

    /**
     * view转bitmap
     */
    private static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            String character_set, String error_correction_level,
                                            String margin, int color_black, int color_white) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black;//黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white;// 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
