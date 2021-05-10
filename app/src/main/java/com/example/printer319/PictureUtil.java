package com.example.printer319;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import zpSDK.zpSDK.zpBluetoothPrinter;

public class PictureUtil {
    private static PictureUtil sPictureUtil = null;
    private int repeatCode = 0;
    private  static Context mContext;

    public static PictureUtil getInstance(Context context) {
        mContext = context;
        if (null == sPictureUtil) {
            sPictureUtil = new PictureUtil();
        }
        return sPictureUtil;
    }

    private zpBluetoothPrinter mBigP;
    private boolean connect;

    private PictureUtil() {
        mBigP = new zpBluetoothPrinter(mContext);
    }

    public boolean connectBT(String mac) {
        Log.d("PictureUtil", "connectBT");
        synchronized (mBigP) {
            if (connect) {
                disconnectBT();
            }
            if (!mBigP.connect(mac)) {
                Log.d("PictureUtil", "connectBT failed");
                return false;
            }
            Log.d("PictureUtil", "connectBT success");
            connect = true;
//            repeatCode = Prefs.getInstance().getInt(AppConstants.PREFS_REPEAT_CODE, 0);

        }
        return true;
    }

    public boolean isConnect() {
        return connect;
    }

    public void disconnectBT() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("PictureUtil", "disconnectBT");
        synchronized (mBigP) {
            if (null != mBigP) {
                mBigP.disconnect();
                connect = false;
//                Prefs.getInstance().putInt(AppConstants.PREFS_REPEAT_CODE, repeatCode);

            }
        }

    }

    /**
     * @param activity
     * @param categoryBean
     * @param isReplenish
     * @return
     */
    public void printPicture(Activity activity) {
        //前2个参数直接为0，就是从原点开始打印，最好不要改动。后面的参数是图片的尺寸。
//        Log.d("PictureUtil", "printPicture" + new Gson().toJson(categoryBean));
        synchronized (mBigP) {
//            repeatCode++;
//            if (repeatCode > 99998) {
//                repeatCode = 0;
//            }
            // mBigP.drawGraphic(10, 10, 480, 320, getViewBitmap(activity, categoryBean, repeatCode, isReplenish));
            Log.d("PictureUtil","send data start");
            mBigP.pageSetup(480, 310);
            mBigP.drawGraphic(5, 5, 460, 290, getViewBitmap(activity));
            mBigP.print(0, 0);
            Log.d("PictureUtil","send data finish");
        }
    }

    /**
     * View2Bitmap
     */
    public static Bitmap getViewBitmap(Activity activity) {

//        String str2 = repeatCode + "";
//        String str3 = "";
//        if (str2.length() < 5) {
//            for (int j = 0; j < 5 - str2.length(); j++) {
//                str3 = str3 + "0";
//            }
//            str2 = str3 + str2;
//        }
        //将布局转化成view对象
        View viewBitmap = LayoutInflater.from(activity).inflate(R.layout.print_view, null);

        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        viewBitmap.layout(0, 0, width, height);
        int measuredWidth = 200;
        int measuredHeight = 200;

        viewBitmap.measure(measuredWidth, measuredHeight);
        viewBitmap.layout(0, 0, viewBitmap.getMeasuredWidth(), viewBitmap.getMeasuredHeight());

        viewBitmap.setDrawingCacheEnabled(true);
        viewBitmap.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        viewBitmap.setDrawingCacheBackgroundColor(Color.WHITE);

        ImageView imageView = viewBitmap.findViewById(R.id.print_img_1);
        TextView tvName = viewBitmap.findViewById(R.id.print_1);
        TextView tvBatchName = viewBitmap.findViewById(R.id.print_2);
        TextView tvbatch = viewBitmap.findViewById(R.id.print_txt_1);
        TextView tvExpiredTime = viewBitmap.findViewById(R.id.print_txt_3);
        TextView tvVegetables = viewBitmap.findViewById(R.id.print_5);
        TextView tvWeek = viewBitmap.findViewById(R.id.print_6);
        TextView tvAvailable = viewBitmap.findViewById(R.id.print_txt_2);
        TextView textView = viewBitmap.findViewById(R.id.print_7);
        TextView tvRepeatCode = viewBitmap.findViewById(R.id.print_8);
        TextView tvStoreRoom = viewBitmap.findViewById(R.id.print_4);

//        String name = categoryBean.getCategoryName();
//        if (name.indexOf("-") != -1){
//            name = name.substring(0, name.indexOf("-"));
//        }
        tvName.setText("冷冻厚切调理澳洲牛排");
        tvbatch.setText("20-01-19 06:35");
        tvExpiredTime.setText("20-01-19");
        tvWeek.setText("5");
//        tvRepeatCode.setText(str2);
//        if (isReplenish) {
//            tvVegetables.setVisibility(View.VISIBLE);
//        } else {
//            tvVegetables.setVisibility(View.GONE);
//        }
//        if ("鸡库".equals(categoryBean.getStoreRoom())){
//            tvStoreRoom.setText("鸡库");
//        }else {
        tvStoreRoom.setText("菜库");
//        }
//        String available = "";
//        if (!TextUtils.isEmpty(categoryBean.getPrecoolTime())) {
//            String precoolToAvailableTime = TimeUtils.stampToDate(TimeUtils.dateToStamp(categoryBean.getBatch())
//                    + (Integer.parseInt(categoryBean.getPrecoolTime())) * 60L * 1000L);
//            categoryBean.setAvailableTime(precoolToAvailableTime);
//            tvExpiredTime.setText("详见包装标签");
//        }
//        if (!TextUtils.isEmpty(categoryBean.getAvailableTime())) {
        tvAvailable.setText("20-01-19 06:35");
//            available = "\n可用时间:" + categoryBean.getAvailableTime();
//            textView.setVisibility(View.VISIBLE);
        tvAvailable.setVisibility(View.VISIBLE);
//        } else {
//            tvAvailable.setVisibility(View.GONE);
//            textView.setVisibility(View.GONE);
//        }

//        String text = "产品名称:" + categoryBean.getCategoryName() + "\n产品编码:" + categoryBean.getCategoryCode()
//                + "\n批次号:" + categoryBean.getBatch() + "\n到期时间:" + categoryBean.getExpiredTime() + available + "\n标签:" + repeatCode;
//        Log.e("PictureUtil", "getViewBitmap: 打印字符串" + text + "   ");
        imageView.setImageBitmap(Create2DCode("冷冻厚切调理澳洲牛排\n存储时间：20-01-19 06：35\n到期时间20-01-19 06：35\n可用时间：20-01-19 06：35 \n菜库", 200, 200, "UTF-8", "M", "0", Color.BLACK, Color.WHITE));
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
        /** 1.参数合法性判断 */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }

        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }

        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();

            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }

            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level); // 容错级别设置
            }

            Log.e("stone", "createQRCodeBitmap: " + margin);
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap Create2DCode(String content, int width, int height,
                                      String character_set, String error_correction_level,
                                      String margin, int color_black, int color_white) {

        /** 1.参数合法性判断 */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }

        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }

            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level); // 容错级别设置
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
            }
            BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            matrix = deleteWhite(matrix);//删除白边
            width = matrix.getWidth();
            height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = color_black;
                    } else {
                        pixels[y * width + x] = color_white;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

}