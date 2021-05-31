package com.example.printer319;


import zpSDK.zpSDK.zpBluetoothPrinter;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static BluetoothAdapter myBluetoothAdapter;
    public String SelectedBDAddress;
    public static EditText inputText;
    public static String BStr = "gbk";
    String cpclString = "! 0 200 200 80 1" + "\n" +
            "PAGE-WIDTH 574" + "\n" +
            "T 24 0 200 10 " + "UROVO优博讯" + "\n" +
            "PRINT" + "\n";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ListBluetoothDevice())
            Toast.makeText(this, "can not find Bluetooth Adapter!", Toast.LENGTH_LONG).show();
        Button btnString = findViewById(R.id.buttonString);
        Button btnCPCL = findViewById(R.id.buttonCPCL);
        Button btn1Dplus = findViewById(R.id.button1Dplus);
        Button btn2Dplus = findViewById(R.id.button2Dplus);
        Button btnPicture = findViewById(R.id.buttonPic);
        Button btnSend = findViewById(R.id.buttonSend);
        Button btnESC = findViewById(R.id.buttonESC);
        Button btnFeed = findViewById(R.id.buttonFeed);
        Button btnSample = findViewById(R.id.btn_sample);
        inputText = findViewById(R.id.edt1);

        btnString.setOnClickListener(this);
        btnCPCL.setOnClickListener(this);
        btnESC.setOnClickListener(this);
        btn1Dplus.setOnClickListener(this);
        btn2Dplus.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnFeed.setOnClickListener(this);
        btnSample.setOnClickListener(this);
    }

    public boolean ListBluetoothDevice() {
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        ListView listView = (ListView) findViewById(R.id.listView1);
        SimpleAdapter m_adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{"DeviceName", "BDAddress"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(m_adapter);



        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            Toast.makeText(this, "can not find the BT adaptor", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }

        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) return false;
        for (BluetoothDevice device : pairedDevices) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("DeviceName", device.getName());
            map.put("BDAddress", device.getAddress());
            list.add(map);
        }
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                SelectedBDAddress = list.get(arg2).get("BDAddress");
                if (((ListView) arg0).getTag() != null) {
                    ((View) ((ListView) arg0).getTag()).setBackgroundDrawable(null);
                }
                ((ListView) arg0).setTag(arg1);
                arg1.setBackgroundColor(Color.BLUE);

            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonFeed:
                if (SelectedBDAddress != null) {
                    SDKFeed(SelectedBDAddress);
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonString:
                inputText.setText("sdk,drawtext:UROVO优博讯");
                if (SelectedBDAddress != null) {
                    SDKDemo(SelectedBDAddress, inputText.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonCPCL:
                //Toast.makeText(this,"Instruction",Toast.LENGTH_LONG).show();
                inputText.setText(cpclString);
                if (SelectedBDAddress != null) {
                    sendText(SelectedBDAddress, "! 0 200 200 400 1\n" +
                            "GAP-SENSE\n" +
                            "SETBOLD 2\n" +
                            "SETMAG 2 2 \n" +
                            "T 24 0 16 24 惠州-惠东1仓网格站\n" +
                            "SETBOLD 1\n" +
                            "SETMAG 1 1 \n" +
                            "B 128 2 1 75 16 80 11234567890643\n" +
                            "T 24 0 152 165 11234567890643\n" +
                            "SETSP 2\n" +
                            "ML 35\r\n" +
                            "T 24 0 16 230 " +
                            "托盘：TP00933\r\n" +
                            "SKU：59\r\n" +
                            "容器数：4\r\n" +
                            "打印时间：2021-04-02T22:11:59.305\r\n" +
                            "ENDML\r\n" +
                            "SETSP 0\n" +
                            "SETSP 2\n" +
                            "ML 35\r\n" +
                            "T 24 0 240 264 " +
                            "SKU件数：519\r\n" +
                            "打印人：卢子红\r\n" +
                            "ENDML\r\n" +
                            "SETSP 0\n" +
                            "FORM\n" +
                            "PRINT\n");
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonESC:
                //Toast.makeText(this,"Instruction",Toast.LENGTH_LONG).show();
                if (SelectedBDAddress != null) {
                    printEscDemo(SelectedBDAddress);
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button1Dplus:
                if (SelectedBDAddress != null) {
                    print1DBarcode();
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button2Dplus:
                if (SelectedBDAddress != null) {
                    print2DBarcode();
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonPic:
                if (SelectedBDAddress != null) {
                    printPicture();
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonSend:
                String input = inputText.getText().toString();
                if (SelectedBDAddress != null) {
                    sendText(SelectedBDAddress, input);
                } else {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_sample:
                if (SelectedBDAddress == null) {
                    Toast.makeText(MainActivity.this, "no printer！", Toast.LENGTH_LONG).show();
                }
                try {
                    printSample(SelectedBDAddress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void printSample(String mac) throws InterruptedException {
        final zpBluetoothPrinter printer = new zpBluetoothPrinter(this);
        if (!printer.connect(mac)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
                printer.pageSetup(609, 350);
        printer.drawText(10, 10, "打印机测试文字", 1, 0, 0, false, false);
        printer.drawText(10, 30, "打印机测试文字", 2, 0, 0, false, false);
        printer.drawText(10, 80, "打印机测试文字", 3, 0, 0, false, false);
        printer.drawText(10, 120, "打印机测试文字", 4, 0, 0, false, false);
        printer.drawText(10, 200, "打印机测试文字", 5, 0, 0, false, false);
        printer.print(0, 0);
        printer.disconnect();
    }


    //command
    public void sendText(String SelectedBDAddress, String input) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(this);
        if (!zpSDK.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            zpSDK.Write(new byte[]{0x1B, 0x74, (byte) 0xff});
            zpSDK.Write(input.getBytes(BStr));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zpSDK.Write(new byte[]{0x0d, 0x0a});
        zpSDK.disconnect();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void SDKFeed(String SelectedBDAddress) {
                zpBluetoothPrinter mBigP = new zpBluetoothPrinter(this);
            if(mBigP.connect(SelectedBDAddress)) {//00:40:11:33:09:68
                //for (int i =0;i<200;++i)
                {
                    mBigP.pageSetup(600, 300);
                    mBigP.drawText(300,120, "11111111",1,0,0,false,false);
                    mBigP.print(0, 1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBigP.disconnect();
            }

    }

    public void SDKDemo(String SelectedBDAddress, String input) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(this);
        if (!zpSDK.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
        zpSDK.pageSetup(574, 110);
        zpSDK.drawText(20, 0, input, 2, 0, 0, false, false);
        zpSDK.print(0, 1);
        zpSDK.disconnect();
    }



    public void printEscDemo(String SelectedBDAddress) {
        zpBluetoothPrinter mESCPrinter=new zpBluetoothPrinter(this);
        if (!mESCPrinter.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            for (int i = 0; i < 1; i++) {
                mESCPrinter.Write(new byte[]{0x1B, 0x40});        //reseting printer,初始化打印机
                mESCPrinter.Write(new byte[]{0x1b, 0x21, 0x00});
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});
                mESCPrinter.Write(new byte[]{0x30, 0x31, 0x32, 0x33});
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x02});    //CHOOSE 32*32
                mESCPrinter.Write(new byte[]{0x1b, 0x61, 0x00}); //set MIDDLE，对齐方式（居中）
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline，设置/取消下划线打印
                mESCPrinter.Write(new byte[]{0x1b, 0x45, 0x01});    //set BOLD，设置粗体打印
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x14});    //line width 0.125*3，设置行间距
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write("Illegal parking notice\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x45, 0x00});    //cancel BOLD，取消粗体打印
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x00});    //line width 0.125:0
                mESCPrinter.Write(new byte[]{0x1d, 0x68, 0x50});//bar code height set to 10mm，条码高度
                mESCPrinter.Write(new byte[]{0x1d, 0x77, 0x02});//bar code width set to 3，条码窄条比
                mESCPrinter.Write(new byte[]{0x1d, 0x48, 0x02});//识别符显示位置为正下方
                mESCPrinter.Write(new byte[]{0x1d, 0x66, 0x00});
                mESCPrinter.Write(new byte[]{0x1D, 0x6B, 0x49, 0x0A, '6', '0', '0', '0', '1', '0', '0', '0', '8', '6'});//:ӡcodebar�룺*6000100086*
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x10});    //line width 0.125:8*2
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x01});    //choose 16*16 ziku
                mESCPrinter.Write("*  6  0  0  0  1  0  0  0  8  6  *\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x61, 0x0}); //choose the left
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x00});    //choose 24*24 ziku
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x8});    //line width 0.125*4
                mESCPrinter.Write("Law enforcement code:310118\n".getBytes("GBK"));
                mESCPrinter.Write("Vehicle brand:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("huE29146".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("     color:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("black\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("vehicle type:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("car\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("Illegal stopping time:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});//underline
                mESCPrinter.Write("2018".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("/".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("03".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});//cancel underline
                mESCPrinter.Write("/".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("31".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write(".".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("17".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("h".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("15".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("min\n".getBytes("GBK"));
                mESCPrinter.Write("Illegal parking:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("The intersection of outside qingsong road and qinghu road is about n200 meters\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write(" If the motor vehicle parking at the above time and place violates the provisions of article 56 of road traffic safety, please present this notice to the traffic police detachment of qingpu district public security bureau for handling within 3 .s and 15 .s.(venue: 8938 north qingsong highway, time: Mon. to Satur., 8:30-16:30))\n".getBytes("GBK"));
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write(String.format("%40s\n", "2018/3mon31").getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x01});    //choose 16*16 ziku
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x10});    //line width 0.125*8:2
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("                                                                       \n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x08});    //line width 0.125*8:1
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("Note: 1. If the domicile address or contact number of all levels of motor vehicles has changed, please apply to the vehicle management office at the place of registration for change and filing in time.\n".getBytes("GBK"));
                mESCPrinter.Write("2. Those who hold a peony card and have no objection to the illegal facts can handle the traffic violation and pay the penalty through telephone bank (95588), online bank (www.icbc.com.cn or www.sh.icbc.com.cn) and multimedia self-service terminal\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }//直接发送输入框内容
        mESCPrinter.disconnect();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void print1DBarcode() {

        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(this);
        if (!zpSDK.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
        zpSDK.pageSetup(574, 200);
        zpSDK.drawBarCode(10, 40, "X11JMO016DR450170", 128, 0, 1, 50);
        zpSDK.drawText(10, 5, "X11JMO016DR450170", 1, 0, 0, false, false);
        zpSDK.barcodeText(0,1,5,0,1,0,50,20,20,"X11JMO016DR450170");
        zpSDK.drawBarCode(10, 40, "X11JMO016DR450170", 128, 0, 1, 50);
        zpSDK.print(0, 1);
        zpSDK.disconnect();

    }

    public void print2DBarcode() {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(this);
        if (!zpSDK.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }
        zpSDK.pageSetup(574, 180);
        zpSDK.drawQrCode(230, 10, "http://en.urovo.com", 0, 4, 3);
        zpSDK.print(0, 0);

        zpSDK.disconnect();
    }

    public void printPicture() {

//            //   mHandler.sendEmptyMessage(0);
//      new Thread() {
//            @Override
//            public void run() {
//                if (!PictureUtil.getInstance(MainActivity.this).connectBT(SelectedBDAddress)) {
//                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
//                    Log.v("zpSDK", "连接失败！");
//                    return;
//                }
//                //for (int i = 0; i < 1; i++)
//                {
//                    PictureUtil.getInstance(MainActivity.this).printPicture(MainActivity.this);
//                }
//                PictureUtil.getInstance(MainActivity.this).disconnectBT();
//            }
//        }.start();
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(this);
        if (!zpSDK.connect(SelectedBDAddress)) {
            Toast.makeText(this, "connect fail------", Toast.LENGTH_LONG).show();
            return;
        }

        zpSDK.pageSetup(580, 600);


        /**---------------打印图片-------------------------*/

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inDensity = this.getResources().getDisplayMetrics().densityDpi;
        opts.inTargetDensity = this.getResources().getDisplayMetrics().densityDpi;
        Bitmap img = BitmapFactory.decodeResource(this.getResources(), R.drawable.test, opts);
        zpSDK.drawGraphic(30, 20, 0, 0, img);

        /**------------------------打印水印------------------*/
        zpSDK.bkText(24,2,50,50,110,"2",0);
        /**------------------------字体放大------------------*/
        zpSDK.setMag(5,5);
        /**------------------------打印水印------------------*/
        zpSDK.bkText(24,4,300,200,110,"R",0);
        /**------------------------字体不放大-----------------*/
        zpSDK.setMag(0,0);

        zpSDK.print(0,1);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zpSDK.disconnect();

    }
}
