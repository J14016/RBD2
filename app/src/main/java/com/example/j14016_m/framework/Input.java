package com.example.j14016_m.framework;

import java.util.List;

/**
 * Created by J14016_M on 2016/01/19.
 */
public interface Input {
    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;
    }

    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public float getAccelX();
    public float getAccelY();
    public float getAccelZ();
    public List<TouchEvent> getTouchEvents();
}
