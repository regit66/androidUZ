package com.ajaybadgujar.roadracerlite;

public class TexCar extends Texture {

	private float track;
	private float position;
	private float speed;

	private static float texture[] = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f
	};

	public TexCar(float startPosition, float startTrack)
	{
		super(texture);

		position = startPosition;
		track = startTrack;
	}

	public float getTrack() { return track; }
	public void setTrack(float track) { this.track = track; }

	public  float getPosition() { return position; }
	public void setPosition(float position) { this.position = position; }

	public float getSpeed(){ return speed; }
	public void setSpeed(float speed){ this.speed = speed; }

	public Bounds getCurrentBounds(){

		Bounds bounds = new Bounds();

		float v = 0.4f;
		float h = 0.05f;

		bounds.Top = position + h;
		bounds.Down = position - h;
		bounds.Left = track - v;
		bounds.Right = track + v;

		return bounds;
	}
}
