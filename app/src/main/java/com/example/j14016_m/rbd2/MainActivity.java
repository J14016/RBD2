package com.example.j14016_m.rbd2;

import com.example.j14016_m.framework.impl.GLGame;
import com.example.j14016_m.framework.impl.GLScreen;

public class MainActivity extends GLGame {

    @Override
    public GLScreen getStartScreen() {
        return new GameScreen(this);
    }
}
