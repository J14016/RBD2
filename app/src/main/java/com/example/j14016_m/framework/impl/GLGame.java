package com.example.j14016_m.framework.impl;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.j14016_m.framework.Audio;
import com.example.j14016_m.framework.FileIO;
import com.example.j14016_m.framework.Game;
import com.example.j14016_m.framework.Input;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/19.
 */
public abstract class GLGame extends Activity implements Game, GLSurfaceView.Renderer {
    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    private GLSurfaceView glView;
    private GLScreen screen;
    private Audio audio;
    private GLGraphics glGraphics;
    private Input input;
    private FileIO fileIO;
    private Object stateChanged = new Object();
    private GLGameState state = GLGameState.Initialized;
    private long startTime = System.nanoTime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBOpenHelper.create(this);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glView = new GLSurfaceView(this);
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glView.setRenderer(this);
        setContentView(glView);

        glGraphics = new GLGraphics(glView);

        input = new AndroidInput(this, glView, 1, 1);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
    }

    public GLGraphics getGLGraphics() {
        return glGraphics;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO () { return fileIO; }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public GLScreen getCurrentScreen() {
        return screen;
    }

    @Override
    public void setScreen(GLScreen screen) {
        if(screen == null) {
            throw new IllegalArgumentException("Screen must not be null");
        }

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;

    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onPause() {
        synchronized(stateChanged) {
            if(isFinishing()) {
                state = GLGameState.Finished;
            } else {
                state = GLGameState.Paused;
            }
            while(true) {
                try {
                    stateChanged.wait();
                    break;
                } catch(InterruptedException e) {
                }
            }
            glView.onPause();
            super.onPause();

        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glGraphics.setGL(gl10);
        synchronized(stateChanged) {
            if(state == GLGameState.Initialized) {
                screen = getStartScreen();
            }
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        gl10.glViewport(0, 0, i, i1);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLGameState state = null;
        synchronized(stateChanged) {
            state = this.state;
        }

        if(state == GLGameState.Running) {
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if(state == GLGameState.Paused) {
            screen.pause();
            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if(state == GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            DBOpenHelper.destroy();
            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }
    }


}
