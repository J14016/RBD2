package com.example.j14016_m.framework.gl;

import com.example.j14016_m.framework.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J14016_M on 2016/01/23.
 */
public class Vertex {
    public Vector3 p;
    public Vector3 norm;
    public float u, v;

    public Vertex() {
        p = new Vector3();
        norm = new Vector3();

        p.x = 0;
        p.y = 0;
        p.z = 0;
        norm.x = 0;
        norm.y = 0;
        norm.z = 0;
        u = 0;
        v = 0;

    }

    public Vertex(float px, float py, float pz, float nx, float ny, float nz, float u, float v) {
        p = new Vector3();
        p.x = px;
        p.y = py;
        p.z = pz;

        norm = new Vector3();
        norm.x = nx;
        norm.y = ny;
        norm.z = nz;

        this.u = u;
        this.v = v;
    }

    public Vertex(Vector3 p, Vector3 n, float u, float v) {
        this.p = p;
        this.norm = n;
        this.u = u;
        this.v = v;
    }

}
