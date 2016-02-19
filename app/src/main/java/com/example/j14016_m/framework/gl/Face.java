package com.example.j14016_m.framework.gl;

import com.example.j14016_m.framework.math.Vector3;

/**
 * Created by J14016_M on 2016/01/23.
 */
public class Face {

    public int i0, i1, i2;
    public float u0, v0, u1, v1, u2, v2;
    public Vector3 norm;

    public Face(int i0, int i1, int i2, float u0, float v0, float u1, float v1, float u2, float v2, Vector3 norm)  {
        this.i0 = i0;
        this.i1 = i1;
        this.i2 = i2;

        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;

        this.norm = norm;
    }

}
