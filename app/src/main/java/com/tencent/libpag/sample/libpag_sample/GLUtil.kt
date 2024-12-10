package com.tencent.libpag.sample.libpag_sample

import android.opengl.GLES20


object GLUtil {

    private val TAG = "GLUtil"

    fun loadShader(type: Int, shaderCode: String?): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    fun checkGlError(glOperation: String) {
        var error: Int
        while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
            throw RuntimeException("$glOperation: glError $error")
        }
    }

    fun buildProgram(vertexSource: String?, fragmentSource: String?): Int {
        val vertexShader = buildShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }
        val fragmentShader = buildShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragmentShader == 0) {
            return 0
        }
        val program = GLES20.glCreateProgram()
        if (program == 0) {
            return 0
        }
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
        return program
    }

    fun buildShader(type: Int, shaderSource: String?): Int {
        val shader = GLES20.glCreateShader(type)
        if (shader == 0) {
            return 0
        }
        GLES20.glShaderSource(shader, shaderSource)
        GLES20.glCompileShader(shader)
        val status = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            GLES20.glDeleteShader(shader)
            return 0
        }
        return shader
    }
}