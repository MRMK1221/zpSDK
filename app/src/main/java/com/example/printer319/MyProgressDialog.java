package com.example.printer319;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * create by 大白菜
 * description
 */
public class MyProgressDialog extends Dialog {


    private TextView tvTitle;
    private ProgressBar pbProgress;
    private ImageView ivStatus;
    private Button btn_confirm;
    private String mTitle;

    public MyProgressDialog(@NonNull Context context) {
        super(context);
    }

    public MyProgressDialog(@NonNull Context context, int themeResId, String title) {
        super(context, themeResId);
        mTitle = title;
    }

    protected MyProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (int) (display.getWidth() * 0.5);
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        tvTitle.setText(mTitle);
        ivStatus.setVisibility(View.INVISIBLE);
        btn_confirm.setEnabled(false);
        pbProgress.setVisibility(View.VISIBLE);
        ivStatus.setVisibility(View.INVISIBLE);
    }

    public void failed(String strErr) {
        if (isShowing()) {
            tvTitle.setText(strErr);
            pbProgress.setVisibility(View.INVISIBLE);
            ivStatus.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.drawable.ic_check_error);
            pbProgress.setVisibility(View.INVISIBLE);
            btn_confirm.setEnabled(true);
        }
    }

    public void success(String strSuccess) {
        if (isShowing()){
            tvTitle.setText(strSuccess);
            pbProgress.setVisibility(View.INVISIBLE);
            ivStatus.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.drawable.ic_check_ok);
            pbProgress.setVisibility(View.INVISIBLE);
            btn_confirm.setEnabled(true);
        }
    }


}
