package com.tencent.libpag.sample.libpag_sample

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.service.wallpaper.WallpaperService
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.WindowManager


const val TAG = "LiveWallpaperService"

/**
 * 动态设置壁纸服务
 */
class LiveWallpaperService : WallpaperService() {

    private lateinit var windowManager: WindowManager

    override fun onCreate() {
        windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        super.onCreate()
    }

    override fun onCreateEngine(): Engine {
        return GoEngine()
    }

    /**
     * 塔智 go 绘制引擎
     */
    inner class GoEngine : Engine() {

        /**
         * 最终要绘制的目录: /data/user/10/com.avatr.ivi.digital/wallpaper
         */
        private lateinit var wallpaperSurfaceView: WallpaperSurfaceView
        private lateinit var h: SurfaceHolder

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            if (surfaceHolder != null) {
                h = surfaceHolder
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            val layoutParams = WindowManager.LayoutParams(
                1920,
                1080,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            wallpaperSurfaceView = WallpaperSurfaceView(
                this@LiveWallpaperService,
                displayId = 0,
            )
            wallpaperSurfaceView.requestRender()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
            } else {
            }
        }

        override fun onDestroy() {
            super.onDestroy()
        }

        inner class WallpaperSurfaceView(
            context: Context, attrs: AttributeSet? = null,
            displayId: Int,
        ) : GLSurfaceView(context, attrs) {
            init {
                setEGLContextClientVersion(3)
                setRenderer(GLRender(context))
                renderMode = RENDERMODE_CONTINUOUSLY
            }

            override fun getHolder(): SurfaceHolder {
                return h
            }

            fun onWallpaperDestroy() {
                super.onDetachedFromWindow()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}

