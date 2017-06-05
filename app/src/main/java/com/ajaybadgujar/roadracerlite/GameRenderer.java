package com.ajaybadgujar.roadracerlite;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameRenderer  implements Renderer{

	private GameActivity gameView;
	private TexRoad road = new TexRoad();
	private TexCar player = new TexCar();
	private TexCar enemy = new TexCar();
	private TexCar enemy2 = new TexCar();
	private TexCar enemy3 = new TexCar();
	private TexController accelerator = new TexController();
	private TexController breaks = new TexController();
	private GL10 gl;
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	
	// Car horizontal position limit in scale
	private static float carLLimit = 1.8f;
	private static float carRLimit = 3.8f;
	private static float carCenterPos = 2.8f;
	private float carCurrentPos = 2.8f;
	private float roadYOffset = 0.0f;
	private float carSpeed = 0.0f;
	private float speedenemy=10;
    private int i1;




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
		DrawCar(gl);







		DrawAccel(gl);
		DrawBreaks(gl);


		DrawEnemy(enemy, gl, i1);
		DrawEnemy(enemy, gl, i1);
		DrawEnemy(enemy2, gl, i1);
		DrawEnemy(enemy3, gl, i1);
		//DrawEnemy(new TexCar(), gl, i1);






		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}



	private void ScrollRoad(){

		switch(Global.PLAYER_ACTION){
		case Global.ACCELERATOR_PRESSED:
			if(roadYOffset < 1.0f){ // reset road texture position
				roadYOffset += carSpeed;
				if(carSpeed < 0.05f){
				 carSpeed += 0.0002f;
				}
			}else{
				roadYOffset -= 1.0f;
			}
			break;
		case Global.BREAKS_PRESSED:
			if(carSpeed > 0.0f){
				roadYOffset += carSpeed;
				carSpeed -= 0.001;
			}else{
				carSpeed = 0.0f;
			}		
			break;
		case Global.CONTROL_RELEASED:			
			if(carSpeed > 0.0f){
				roadYOffset += carSpeed;
				carSpeed -= 0.0002;
			}else{
				carSpeed = 0.0f;
			}			
			break;		
		}
	}
	
	public void DrawRoad(GL10 gl){
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

	private void MoveCar(){

		if(Global.PLAYER_ACTION != Global.ACCELERATOR_PRESSED) return;
		
		if(Global.SENSORE_ACCELEROMETER_X > 0.5 ){
			if(carCurrentPos > carLLimit){
				carCurrentPos = carCurrentPos - (float)Global.SENSORE_ACCELEROMETER_X/25;
			}else{
				carCurrentPos = carLLimit;
			}
		}else if(Global.SENSORE_ACCELEROMETER_X < -0.5 ){
			if(carCurrentPos < carRLimit){
				carCurrentPos = carCurrentPos - (float)Global.SENSORE_ACCELEROMETER_X/25;
			}else{
				carCurrentPos = carRLimit;
			}
		}else{
			
		}
	}



	public void DrawEnemy(TexCar car,GL10 gl ,float il){

		Log.i("ddd","ddd"+ carCurrentPos);
		if (car.getSpedd()<=-1)
		{Random r = new Random();
			car.setTrack(r.nextInt(3) + 2);
			car.setSpedd(10);

		}
		if (car.getSpedd()<=2 && car.getTrack()==carCurrentPos)
		{

			Log.i("ddd","ddd"+ carCurrentPos);
			GameActivity.getInstance().finish();

		}
		//MoveCarEnemy();
	//	for (int i=1; i<10; i++) {

		Random r = new Random();
		float minX = 0.01f;
		float maxX = 0.025f;
		float finalX = r.nextFloat() * (maxX - minX) + minX;
		car.setSpedd(car.getSpedd()-finalX);


		if (car.getTrack()==0) {
			car.setTrack(r.nextInt(2) + 1);
			car.setTrack(car.getTrack()+ 0.8f);
		}





		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.15f, Global.getProportionateHeight(0.15f), .15f);

		gl.glTranslatef(car.getTrack(), car.getSpedd(), 0f);

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);

		car.draw(gl);
		gl.glPopMatrix();

		gl.glLoadIdentity();

	}



	public void DrawCar(GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.15f, Global.getProportionateHeight(0.15f), .15f);
		gl.glTranslatef(carCurrentPos, 1f, 0f);

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);

		player.draw(gl);
		gl.glPopMatrix();
		
		gl.glLoadIdentity();
		gl.glActiveTexture(Global.CAR);
	}
	
	public void DrawAccel(GL10 gl){
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

	
	public void DrawBreaks(GL10 gl){
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
		road.loadTexture(gl, Global.ROAD , Global.context);
		player.loadTexture(gl, Global.CAR, Global.context);
		enemy.loadTexture(gl, Global.CAR, Global.context);
		enemy2.loadTexture(gl, Global.CAR, Global.context);
		enemy3.loadTexture(gl, Global.CAR, Global.context);
		accelerator.loadTexture(gl, Global.ACCELERATOR, Global.context);
		breaks.loadTexture(gl, Global.BREAKS, Global.context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// Enable game screen width and height to access other functions and classes
		Global.GAME_SCREEN_WIDTH = width;
		Global.GAME_SCREEN_HEIGHT = height;
		
		// set position and size of viewport 
		gl.glViewport(0, 0, width, height);
		
		// Put OpenGL to projectiong matrix to access glOrthof()
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		// Load current identity of OpenGL state
		gl.glLoadIdentity();
		
		// set orthogonal two dimensional rendering of scene
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);		
	}



}
