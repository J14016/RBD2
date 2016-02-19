package com.example.j14016_m.framework.gl;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/30.
 */
public class Mesh {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private int vertexSize;
    private int indexSize;
    private int numVertices;
    private int numIndices;

    private Face faces[];
    private int numFaces;
    private Vertex vertices[];
    private MqoMaterial materials[];
    private int numMaterials;

    private boolean hasNormals;


    public Mesh(Vertex vertices[], int numVertices, Face faces[], int numFaces, MqoMaterial materials[], int numMaterials, boolean hasNormals) {
        this.numVertices = numVertices;
        this.numIndices = numFaces * 3;
        this.numMaterials = numMaterials;
        this.materials = materials;

        this.numFaces = numFaces;
        this.faces = faces;

        this.vertices = vertices;

        this.hasNormals = hasNormals;
        vertexSize = (5 + (hasNormals ? 3 : 0)) * 4;
        int n = vertexSize / 4;


        float vert[] = new float[n * numVertices];
        for(int i = 0; i < numVertices; i++) {
            vert[i * n] = vertices[i].p.x;
            vert[i * n + 1] = vertices[i].p.y;
            vert[i * n + 2] = vertices[i].p.z;
            vert[i * n + 3] = vertices[i].u;
            vert[i * n + 4] = vertices[i].v;
            if(hasNormals) {
                vert[i * n + 5] = vertices[i].norm.x;
                vert[i * n + 6] = vertices[i].norm.y;
                vert[i * n + 7] = vertices[i].norm.z;
            }
        }


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.numVertices * vertexSize );
        byteBuffer.order(ByteOrder.nativeOrder());
        this.vertexBuffer = byteBuffer.asFloatBuffer();
        this.vertexBuffer.put(vert);
        this.vertexBuffer.flip();

        short idx[] = new short[numIndices];
        for(int i = 0; i < numFaces; i++) {
            idx[i * 3] = (short)faces[i].i0;
            idx[i * 3 + 1] = (short)faces[i].i1;
            idx[i * 3 + 2] = (short)faces[i].i2;
        }
        indexSize = 2;
        byteBuffer = ByteBuffer.allocateDirect(this.numIndices * indexSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.indexBuffer = byteBuffer.asShortBuffer();
        this.indexBuffer.put(idx);
        this.indexBuffer.flip();
    }

    private void bind(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertexBuffer.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, vertexSize, vertexBuffer);


        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        vertexBuffer.position(3);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertexBuffer);

        if(hasNormals) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            vertexBuffer.position(5);
            gl.glNormalPointer(GL10.GL_FLOAT, vertexSize, vertexBuffer);
        }
    }

    private void unbind(GL10 gl) {
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        if(hasNormals) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        }
    }

    public void draw(GL10 gl) {
        materials[0].enable(gl);
        bind(gl);
        indexBuffer.position(0);
        gl.glDrawElements(GL10.GL_TRIANGLES, numIndices, GL10.GL_UNSIGNED_SHORT, indexBuffer);
        unbind(gl);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumIndices() {
        return numIndices;
    }

    public int getNumFaces() { return numFaces;}

    public Face[] getFaces() {return faces;}

    public Vertex[] getVertices() { return vertices;}
}
