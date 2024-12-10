package com.tencent.libpag.sample.libpag_sample;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import org.libpag.PAGFile;
import org.libpag.PAGView;

public class CustomPAGView extends View {
    private PAGView pagView;
    private PAGFile pagFile;

    public CustomPAGView(Context context) {
        super(context);
        init();
    }

    public CustomPAGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pagView = new PAGView(getContext(), null, 0);
        pagFile = PAGFile.Load(getContext().getAssets(), "replacement.pag");
        pagView.setComposition(pagFile);
        pagView.setRepeatCount(-1);
//        pagView.play(); // 开始动画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pagView.play(); // 开始动画
//        pagView.draw(canvas); // 在 Canvas 上绘制 PAG 动画
    }

    public void startAnimation() {
//        pagView.play(); // 开始动画
    }

    public void stopAnimation() {
//        pagView.stop(); // 停止动画
    }
}