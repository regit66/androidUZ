package com.ajaybadgujar.roadracerlite;

public class TexCar extends Texture {

	public float getTrack() {
		return track;
	}

	public void setTrack(float track) {
		this.track = track;
	}

	private float track=0;
	private float spedd=10;
	private static float texture[] = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f
	};

	public TexCar(){
		super(texture);
	}

	public  float getSpedd() {
		return spedd;
	}

	public void setSpedd(float spedd) {
		this.spedd = spedd;
	}
}
