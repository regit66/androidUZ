package com.uz.racecar;

/**
 * Created by Marcin on 23.05.2017.
 */

public class Bubble{
    float[] boundingBox;
    int textureNumber;

    Bubble(int n) {
        textureNumber = n;
        boundingBox[0] = 0;
        boundingBox[1] = 100;
        boundingBox[2] = 0;
        boundingBox[3] = 100;
        boundingBox[4] = 100;
        boundingBox[5] = 0;
        boundingBox[6] = 0;
        boundingBox[7] = 0;
        boundingBox[8] = 0;
        boundingBox[9] = 100;
        boundingBox[10] = 0;
        boundingBox[11] = 0;
    }}