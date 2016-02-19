package com.example.j14016_m.framework.impl;

import android.content.Context;
import android.view.View;

import com.example.j14016_m.framework.Input;
import com.example.j14016_m.framework.TouchHandler;

import java.util.List;

/**
 * Created by J14016_M on 2016/01/19.
 */
public class AndroidInput implements Input {
    private AccelerometerHandler accelHandler;
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
