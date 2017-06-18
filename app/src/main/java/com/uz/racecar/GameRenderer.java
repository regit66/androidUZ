package com.uz.racecar;

import android.opengl.GLSurfaceView.Renderer;



import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameRenderer implements Renderer {

    private GameActivity gameView;
    private EnemiesController enemies;

    private TexRoad road = new TexRoad();
    private TexCar player = new TexCar(1.5f, 2.8f);
    private TexController accelerator = new TexController();
    private TexController breaks = new TexController();

    private GL10 gl;
    private long loopStart = 0;
    private long loopEnd = 0;
    private long loopRunTime = 0;

    // Car horizontal position limit in scale
    private static float carLLimit = 1.6f;
    private static float carRLimit = 4f;

    private float roadYOffset = 0.0f;

    @Override
    public void onDrawFrame(GL10 gl) {

        loopStart = System.currentTimeMillis();

        try {
            if (loopRunTime < Global.GAME_THREAD_FPS_SLEEP) {
                Thread.sleep(Global.GAME_THREAD_FPS_SLEEP - loopRunTime);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        ScrollRoad();
        DrawRoad(gl);
        MoveCar();

        DrawRoad(gl);
        DrawEnemies(gl);
        DrawCar(gl);

        DrawAccel(gl);
        DrawBreaks(gl);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void ScrollRoad() {

        float currentSpeed = player.getSpeed();

        switch (Global.PLAYER_ACTION) {

            case Global.ACCELERATOR_PRESSED:
                // reset road texture position
                if (roadYOffset < 1.0f) {
                    roadYOffset += currentSpeed;

                    if (currentSpeed < 0.05f) {
                        player.setSpeed(currentSpeed + 0.0002f);
                    }
                } else {
                    roadYOffset -= 1.0f;
                }
                break;

            case Global.BREAKS_PRESSED:
                if (currentSpeed > 0.0f) {
                    roadYOffset += currentSpeed;
                    player.setSpeed(currentSpeed - 0.001f);
                } else {
                    player.setSpeed(0.0f);
                }
                break;

            case Global.CONTROL_RELEASED:
                if (currentSpeed > 0.0f) {
                    roadYOffset += currentSpeed;
                    player.setSpeed(currentSpeed - 0.0002f);
                } else {
                    player.setSpeed(0.0f);
                }
                break;
        }
    }

    public void DrawRoad(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(1f, 1f, 1f);
        gl.glTranslatef(0f, 0f, 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, roadYOffset, 0.0f);

        road.draw(gl);
        gl.glPopMatrix();

        gl.glLoadIdentity();
    }

    private void MoveCar() {

        if (Global.PLAYER_ACTION == Global.ACCELERATOR_PRESSED) {
            float currentTrack = player.getTrack();

            if (Global.SENSORE_ACCELEROMETER_X > 0.5) {
                if (currentTrack > carLLimit) {
                    player.setTrack(currentTrack - (float) Global.SENSORE_ACCELEROMETER_X / 25);
                } else {
                    player.setTrack(carLLimit);
                }
            } else if (Global.SENSORE_ACCELEROMETER_X < -0.5) {
                if (currentTrack < carRLimit) {
                    player.setTrack(currentTrack - (float) Global.SENSORE_ACCELEROMETER_X / 25);
                } else {
                    player.setTrack(currentTrack);
                }
            }
        }
        if (enemies.isAnyColliding(player)) {
            GameActivity.getInstance().finish();
        }
    }

    private void DrawEnemies(GL10 gl) {

        enemies.update(player);
    }

    public void DrawCar(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(.15f, Global.getProportionateHeight(0.15f), .15f);
        gl.glTranslatef(player.getTrack(), player.getPosition(), 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);

        player.draw(gl);
        gl.glPopMatrix();

        gl.glLoadIdentity();
        gl.glActiveTexture(Global.CAR);
    }

    public void DrawAccel(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(.25f, Global.getProportionateHeight(0.25f), .25f);
        gl.glTranslatef(3f, 0f, 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);

        accelerator.draw(gl);
        gl.glPopMatrix();

        gl.glLoadIdentity();
    }

    public void DrawBreaks(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(.25f, Global.getProportionateHeight(0.25f), .25f);
        gl.glTranslatef(0f, 0f, 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);

        breaks.draw(gl);
        gl.glPopMatrix();

        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Enable 2D maping capability
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glClearDepthf(1.0f);

        // Text depthe of all objects on surface
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        // Enable blend to create transperency
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // Load textures
        road.loadTexture(gl, Global.ROAD, Global.context);
        player.loadTexture(gl, Global.CAR, Global.context);
        enemies = new EnemiesController(gl);

        accelerator.loadTexture(gl, Global.ACCELERATOR, Global.context);
        breaks.loadTexture(gl, Global.BREAKS, Global.context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Enable game screen width and height to access other functions and classes
        Global.GAME_SCREEN_WIDTH = width;
        Global.GAME_SCREEN_HEIGHT = height;

        // set position and size of viewport
        gl.glViewport(10, 0, width, height);

        // Put OpenGL to projectiong matrix to access glOrthof()
        gl.glMatrixMode(GL10.GL_PROJECTION);

        // Load current identity of OpenGL state
        gl.glLoadIdentity();

        // set orthogonal two dimensional rendering of scene
        gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
    }

}
