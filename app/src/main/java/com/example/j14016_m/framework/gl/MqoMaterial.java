package com.example.j14016_m.framework.gl;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/30.
 */
public class MqoMaterial extends Material {
    Texture texture;


    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void enable(GL10 gl) {
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular, 0);
        texture.bind();
    }
}
