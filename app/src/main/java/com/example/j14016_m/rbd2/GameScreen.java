package com.example.j14016_m.rbd2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Matrix;
import android.util.Log;

import com.example.j14016_m.framework.Audio;
import com.example.j14016_m.framework.Input;
import com.example.j14016_m.framework.Music;
import com.example.j14016_m.framework.gl.Camera2D;
import com.example.j14016_m.framework.gl.DirectionalLight;
import com.example.j14016_m.framework.gl.Face;
import com.example.j14016_m.framework.gl.Font;
import com.example.j14016_m.framework.gl.LookAtCamera;
import com.example.j14016_m.framework.gl.Mesh;
import com.example.j14016_m.framework.gl.MqoLoader;
import com.example.j14016_m.framework.gl.SpriteBatcher;
import com.example.j14016_m.framework.gl.Texture;
import com.example.j14016_m.framework.gl.Vertex;
import com.example.j14016_m.framework.impl.DBOpenHelper;
import com.example.j14016_m.framework.impl.GLGame;
import com.example.j14016_m.framework.impl.GLGraphics;
import com.example.j14016_m.framework.impl.GLScreen;
import com.example.j14016_m.framework.math.Collision;
import com.example.j14016_m.framework.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by J14016_M on 2016/01/19.
 */
public class GameScreen extends GLScreen {

    private DirectionalLight directionalLight;
    private LookAtCamera camera;
    private Camera2D guiCamera;

    private Ball ball;
    private Mesh stage;
    private Mesh sky;
    private Mesh goal;
    private Mesh kanban;
    private Music noname;

    private Texture fontTex;
    private Font font;
    private SpriteBatcher batcher;

    private float lastX = -1;
    private float lastY = -1;
    private int deg = 0;

    private String timeStr;
    private boolean goalFlag;
    private boolean gameoverFlag;

    public GameScreen(GLGame glGame) {
        super(glGame);

        fontTex = new Texture(glGame, "items.png", true);
        batcher = new SpriteBatcher(getGLGraphics(), 128);
        font = new Font(fontTex, 224, 0, 16, 16, 20);

        camera = new LookAtCamera(67, getGLGraphics().getWidth() / (float)getGLGraphics().getHeight(), 1, 10000);
        guiCamera = new Camera2D(getGLGraphics(), 480, 320);

        directionalLight = new DirectionalLight();
        directionalLight.setDirection(0, -0.5f, 1);

        ball = new Ball(glGame);
        ball.pos.x = 0;
        ball.pos.y = 10;
        ball.pos.z = 10;

        stage = MqoLoader.load(glGame, "test_stage02.mqo", true);
        sky = MqoLoader.load(glGame, "sky.mqo", false);
        goal = MqoLoader.load(glGame, "goal.mqo", false);
        kanban = MqoLoader.load(glGame, "kanbann.mqo", true);

        Audio audio  = glGame.getAudio();
        noname = audio.newMusic("noname.mid");
        noname.setLooping(true);

        timeStr = String.format("%.2f", 60.0f);
        goalFlag = false;
        gameoverFlag = false;
    }

    @Override
    public void update(float deltaTime) {
        GL10 gl = getGLGraphics().getGL();
        GLGraphics glGraphics = getGLGraphics();

        if(goalFlag) {
            return;
        }

        if(gameoverFlag) {
            return;
        }


        timeStr = String.format("%.2f", Float.parseFloat(timeStr) - deltaTime);
        if(Float.parseFloat(timeStr) <= 0) {
            timeStr = String.format("%.2f", 0.0f);
            gameoverFlag = true;
            return;
        }

        Input input = getInput();



        Vector3 dir = Vector3.sub(camera.getLookAt(), camera.getPosition()).nor();
        Vector3 xDir = Vector3.cross(dir, new Vector3(0, 1, 0)).nor();

        float m[] = new float[16];
        Matrix.setIdentityM(m, 0);

        /*
        m[0] = xDir.x;
        m[1] = xDir.y;
        m[2] = xDir.z;
        m[8] = dir.x;
        m[9] = dir.y;
        m[10] = dir.z;
        */
        m[0] = xDir.x;
        m[4] = xDir.y;
        m[8] = xDir.z;
        m[2] = dir.x;
        m[6] = dir.y;
        m[10] = dir.z;

        float outVec[] = new float[4];
        float inVec[] = new float[4];

        inVec[0] = input.getAccelY() * 1.5f * deltaTime;
        inVec[1] = 0;
        inVec[2] = -((input.getAccelX() - 9) * 1.5f) * deltaTime;
        inVec[3] = 1;

        Matrix.multiplyMV(outVec, 0, m, 0, inVec, 0);


        ball.pos.x += outVec[0];
        ball.pos.z += outVec[2];


        Vector3 pre = new Vector3();
        pre.x = ball.pos.x;
        pre.y = ball.pos.y;
        pre.z = ball.pos.z;
        ball.update(deltaTime);

        Vector3 pos = new Vector3();
        pos.x = ball.pos.x;
        pos.y = ball.pos.y;
        pos.z = ball.pos.z;


        float uvwt[] = new float[4];
        Vector3 v = new Vector3(0, 0, 0);
        v.zero();


        Vector3 n = new Vector3();
        n.zero();
        float mDist = -1;
        for(int i = 0; i < goal.getNumFaces(); i++) {
            Face face = stage.getFaces()[i];
            Vertex v0 = stage.getVertices()[face.i0];
            Vertex v1 = stage.getVertices()[face.i1];
            Vertex v2 = stage.getVertices()[face.i2];

            if (Collision.intersectRayTriangle(pre, pos, v0.p, v1.p, v2.p, uvwt)) {
                v.add(v0.p.x * uvwt[0], v0.p.y * uvwt[0], v0.p.z * uvwt[0]);
                v.add(v1.p.x * uvwt[1], v1.p.y * uvwt[1], v1.p.z * uvwt[1]);
                v.add(v2.p.x * uvwt[2], v2.p.y * uvwt[2], v2.p.z * uvwt[2]);

                float dist = Math.abs(pos.distSquared(v));
                if(mDist <= ball.r) {
                    goalFlag = true;

                    SQLiteDatabase db = DBOpenHelper.getWriteDatabase();
                    Cursor cursor = db.query(DBOpenHelper.TABLE_NAME, null, null, null, null, null, null);

                    int indexTime = cursor.getColumnIndex("time");
                    if(cursor.getCount() == 0) {
                        ContentValues val = new ContentValues();
                        float time = Float.valueOf(timeStr);
                        val.put("time", time);
                        db.insert(DBOpenHelper.TABLE_NAME, null, val);
                        Log.d("insert", "" + time);
                    } else {
                        while (cursor.moveToNext()) {
                            float dbTime = cursor.getFloat(indexTime);
                            float time = Float.parseFloat(timeStr);
                            if(time > dbTime) {
                                ContentValues val = new ContentValues();
                                val.put("time", time);
                                db.update(DBOpenHelper.TABLE_NAME, val, "_id=1", null);
                                Log.d("update", dbTime + "から" + time);
                            }
                        }
                    }

                    cursor.close();
                    db.close();
                }

            }
        }



        v.zero();
        n.zero();
        mDist = -1;
        for(int i = 0; i < stage.getNumFaces(); i++) {
            Face face = stage.getFaces()[i];
            Vertex v0 = stage.getVertices()[face.i0];
            Vertex v1 = stage.getVertices()[face.i1];
            Vertex v2 = stage.getVertices()[face.i2];

            if (Collision.intersectRayTriangle(pre, pos, v0.p, v1.p, v2.p, uvwt)) {
                v.add(v0.p.x * uvwt[0], v0.p.y * uvwt[0], v0.p.z * uvwt[0]);
                v.add(v1.p.x * uvwt[1], v1.p.y * uvwt[1], v1.p.z * uvwt[1]);
                v.add(v2.p.x * uvwt[2], v2.p.y * uvwt[2], v2.p.z * uvwt[2]);


                float dist = Math.abs(pos.distSquared(v));
                if(mDist != -1) {
                    if(dist < mDist) {
                        mDist = dist;
                        if(mDist <= ball.r) {
                            ball.pos.y = v.y + ball.r;
                        }
                    }
                } else {
                    mDist = dist;
                    if(mDist <= ball.r) {
                        ball.pos.y = v.y + ball.r;
                    }
                }

            }

        }


        float touchX = input.getTouchX(0);
        float touchY = input.getTouchY(0);

        if(input.isTouchDown(0)) {
            if(lastX == -1) {
                lastX = touchX;
                lastY = touchY;
            } else {
                float xDist = touchX - lastX;

                lastX = touchX;
                lastY = touchY;

                deg += xDist / 4;
                deg %= 360;

                float x = (float)(Math.sin(Math.toRadians(deg)) * 5.0f + ball.pos.x);
                float z = (float)(Math.cos(Math.toRadians(deg)) * 5.0f + ball.pos.z);
                camera.getPosition().set(x, ball.pos.y + 2, z);


            }
        } else {
            lastX = -1;
            lastY = -1;
            float x = (float)(Math.sin(Math.toRadians(deg)) * 5.0f + ball.pos.x);
            float z = (float)(Math.cos(Math.toRadians(deg)) * 5.0f + ball.pos.z);
            camera.getPosition().set(x, ball.pos.y + 2, z);
        }

        if(ball.pos.y < -20) {
            ball.pos.x = 0;
            ball.pos.y = 10;
            ball.pos.z = 10;
        }
        camera.getLookAt().set(ball.pos.x, ball.pos.y, ball.pos.z);
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = getGLGraphics().getGL();
        GLGraphics glGraphics = getGLGraphics();


        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_TEXTURE_2D);


        camera.setMatrices(gl);
        directionalLight.enable(gl, GL10.GL_LIGHT1);

        ball.draw(gl);

        gl.glPushMatrix();
        stage.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        kanban.draw(gl);
        gl.glPopMatrix();

        directionalLight.disable(gl);



        gl.glDisable(GL10.GL_LIGHTING);
        sky.draw(gl);
        gl.glDisable(GL10.GL_TEXTURE_2D);



        guiCamera.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_TEXTURE_2D);


        batcher.beginBatch(fontTex);
        font.drawText(batcher, timeStr, 10, 320 - 20);
        if(goalFlag) {
            font.drawText(batcher, "goal", 480 / 2, 320 / 2);
        }
        if(gameoverFlag) {
            font.drawText(batcher, "gameover", 480/2, 320/2);
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);


    }

    @Override
    public void pause() {
        noname.pause();
    }

    @Override
    public void resume() {
        noname.play();
    }

    @Override
    public void dispose() {
        noname.dispose();
    }
}
