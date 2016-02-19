package com.example.j14016_m.framework.impl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/19.
 */
public class GLGraphics  {
    private GL10 gl;
    private GLSurfaceView glView;

    GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public GL10 getGL() {
        return gl;
    }

    void setGL(GL10 gl) {
        this.gl = gl;
    }

    public int getWidth() {
        return glView.getWidth();
    }

    public int getHeight() {
        return glView.getHeight();
    }
}
