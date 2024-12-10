package com.tencent.libpag.sample.libpag_sample;

import android.graphics.Canvas;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import androidx.annotation.RequiresApi;
import org.libpag.PAGFile;
import org.libpag.PAGView;

public class MyWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private PAGView pagView;
        // 加载 PAG 文件
        private PAGFile pagFile;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            if (pagView == null) {
                pagView = new PAGView(MyWallpaperService.this);
                pagFile = PAGFile.Load(MyWallpaperService.this.getAssets(), "data_video.pag");
                pagView.setComposition(pagFile);
                pagView.setRepeatCount(-1);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            pagView.setLayoutParams(new FrameLayout.LayoutParams(width, height)); // 设置绘制区域大小
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
//                pagPlayer.prepare();
//                pagPlayer.flush();
//                drawLoop(getSurfaceHolder().getSurface());
//                pagPlayer.setProgress(1);
//                pagPlayer.flush();
                Canvas canvas = null;
                try {
                    canvas = getSurfaceHolder().lockCanvas();
                    if (canvas != null) {
//                        Drawable frame =
//                                getResources().getDrawable(R.mipmap.ic_launcher, null);
//                        frame.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//                        frame.draw(canvas);
                        pagView.draw(canvas);
                        pagView.play();
                    }
                } finally {
                    if (canvas != null) {
                        getSurfaceHolder().unlockCanvasAndPost(canvas);
                    }
                }
            } else {
//                pagView.pause();
            }
        }

    }

}