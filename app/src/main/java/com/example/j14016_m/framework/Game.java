package com.example.j14016_m.framework;

import com.example.j14016_m.framework.impl.GLScreen;

/**
 * Created by J14016_M on 2016/01/19.
 */
public interface Game {

    public Input getInput();
    public FileIO getFileIO();
    public Audio getAudio();
    public GLScreen getCurrentScreen();
    public GLScreen getStartScreen();

    public void setScreen(GLScreen screen);

}
