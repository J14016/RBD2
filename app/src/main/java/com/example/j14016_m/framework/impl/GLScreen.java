package com.example.j14016_m.framework.impl;

import com.example.j14016_m.framework.Game;
import com.example.j14016_m.framework.Input;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/19.
 */
public abstract class GLScreen {
    private final GLGame glGame;

    public GLScreen(GLGame glGame) {
        this.glGame = glGame;
    }

    public abstract void update(float deltaTime);
    public abstract void present(float deltaTime);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();

    public Input getInput() {
        return glGame.getInput();
    }

    public GLGraphics getGLGraphics() {
        return glGame.getGLGraphics();
    }

}
