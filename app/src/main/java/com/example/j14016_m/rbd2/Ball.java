package com.example.j14016_m.rbd2;

import com.example.j14016_m.framework.gl.Mesh;
import com.example.j14016_m.framework.gl.MqoLoader;
import com.example.j14016_m.framework.gl.Vertices3;
import com.example.j14016_m.framework.impl.GLGame;
import com.example.j14016_m.framework.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/24.
 */
public class Ball {
    public Mesh sphere;

    public Vector3 pos;
    public float r = 0.5f;
    public float angle = 0;


    public Ball(GLGame glGame) {
        pos = new Vector3(0, 0, 0);
        sphere = MqoLoader.load(glGame, "sphere.mqo", true);
    }


    public void update(float deltaTime) {
        pos.y += -9.8f * deltaTime;
        angle += 1024 * deltaTime;
    }


    public void draw(GL10 gl) {
        gl.glPushMatrix();
        gl.glRotatef(angle, pos.x, pos.y, pos.z);
        gl.glTranslatef(pos.x, pos.y, pos.z);
        sphere.draw(gl);
        gl.glPopMatrix();
    }


}
