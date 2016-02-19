package com.example.j14016_m.framework;

import android.view.View;

import com.example.j14016_m.framework.Input;

import java.util.List;

/**
 * Created by J14016_M on 2016/01/19.
 */
public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<Input.TouchEvent> getTouchEvents();
}
