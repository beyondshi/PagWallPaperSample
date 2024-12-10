package com.tencent.libpag.sample.libpag_sample

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import org.libpag.PAGFile
import org.libpag.PAGPlayer
import org.libpag.PAGSurface


/**
 * Description:
 */
class GLRender(private var context: Context?) : GLSurfaceView.Renderer {

    private val TAG = "sjj"
    private var textureId = 0
    private lateinit var pagPlayer: PAGPlayer
    private var duration: Long = 0
    var mWidth = 0
    var mHeight = 0
    private var timestamp: Long = 0


    private val VERTEX_MAIN =
        "attribute vec2  vPosition;\n" +
                "attribute vec2  vTexCoord;\n" +
                "varying vec2    texCoord;\n" +
                "\n" +
                "void main() {\n" +
                "    texCoord = vTexCoord;\n" +
                "    gl_Position = vec4 ( vPosition.x, vPosition.y, 0.0, 1.0 );\n" +
                "}";

    private val FRAGMENT_MAIN =
        "precision mediump float;\n" +
                "\n" +
                "varying vec2                texCoord;\n" +
                "uniform sampler2D sTexture;\n" +
                "\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(sTexture, texCoord);\n" +
                "}";

    val SQUARE_COORDS = floatArrayOf(
        1.0f, -1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,
        -1.0f, 1.0f
    )

    val TEXTURE_COORDS = floatArrayOf(
        1f, 1f,
        0f, 1f,
        1f, 0f,
        0f, 0f
    )

    private var mProgram = 0
    var VERTEX_BUF: FloatBuffer? = null
    var TEXTURE_COORD_BUF: FloatBuffer? = null

    fun createFloatBuffer(data: FloatArray): FloatBuffer {
        // 分配内存空间，容量为 float 数组的长度 * 4（一个 float 占 4 个字节）
        val bb = ByteBuffer.allocateDirect(data.size * 4)
        // 使用本机字节序
        bb.order(ByteOrder.nativeOrder())
        // 创建 FloatBuffer
        val floatBuffer = bb.asFloatBuffer()
        // 将 float 数组数据放入 FloatBuffer
        floatBuffer.put(data)

        // 设置 FloatBuffer 的位置为 0
        floatBuffer.position(0)

        return floatBuffer
    }


    private fun initShader() {
        if (VERTEX_BUF == null) {
            VERTEX_BUF = createFloatBuffer(SQUARE_COORDS)
        }
        if (TEXTURE_COORD_BUF == null) {
            TEXTURE_COORD_BUF = createFloatBuffer(TEXTURE_COORDS)

        }
        if (mProgram == 0) {
            mProgram = GLUtil.buildProgram(VERTEX_MAIN, FRAGMENT_MAIN)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        val pagFile = PAGFile.Load(context!!.assets, "wallpaper.pag")
        mWidth = pagFile.width()
        mHeight = pagFile.height()
        duration = pagFile.duration()
        textureId = initRenderTarget()
        val pagSurface = PAGSurface.FromTexture(textureId, mWidth, mHeight)
        pagPlayer = PAGPlayer()
        pagPlayer.composition = pagFile
        pagPlayer.surface = pagSurface
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Log.e(TAG, "width is $width height is $height")
    }

    override fun onDrawFrame(gl: GL10?) {
        Log.e(TAG, "onDrawFrame")

        if (timestamp == 0L) {
            timestamp = System.currentTimeMillis()
        }
        val playTime = (System.currentTimeMillis() - timestamp) * 1000
        pagPlayer.progress = (playTime % duration * 1.0f / duration).toDouble()
        pagPlayer.flush()
        initShader()
        Log.e(TAG, "draw texture id is $textureId")
        GLES20.glUseProgram(mProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        val vPositionLocation = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(
            vPositionLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            VERTEX_BUF
        )
        val vTexCoordLocation = GLES20.glGetAttribLocation(mProgram, "vTexCoord")
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        GLES20.glVertexAttribPointer(
            vTexCoordLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            4 * 2,
            TEXTURE_COORD_BUF
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun initRenderTarget(): Int {
        val id = intArrayOf(0)
        GLES20.glGenTextures(1, id, 0)
        if (id[0] == 0) {
            return 0
        }
        val textureId = id[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GL10.GL_TEXTURE_MIN_FILTER,
            GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GL10.GL_TEXTURE_MAG_FILTER,
            GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            mWidth,
            mHeight,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return textureId
    }
}