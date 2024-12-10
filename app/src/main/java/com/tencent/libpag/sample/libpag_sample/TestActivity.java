package com.tencent.libpag.sample.libpag_sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.libpag.PAGFile;
import org.libpag.PAGView;

public class TestActivity extends AppCompatActivity {

    private CustomPAGView customPAGView;
    private PAGView pagView;
    private PAGFile pagFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        customPAGView = findViewById(R.id.customPagView1);
//        customPAGView.startAnimation(); // 开始动画


        pagView = new PAGView(this);
        pagView.setLayoutParams(new LinearLayout.LayoutParams(150,150));
        LinearLayout view = findViewById(R.id.root);
        view.addView(pagView);
        pagFile = PAGFile.Load(getAssets(), "replacement.pag");
        pagView.setComposition(pagFile);
        pagView.setRepeatCount(-1);
        pagView.play();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        customPAGView.stopAnimation(); // 暂停动画
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        customPAGView.startAnimation(); // 恢复动画
//    }
}
