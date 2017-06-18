package com.uz.racecar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import static com.uz.racecar.Options.NUMBER_OF_ENEMIES;
import static com.uz.racecar.Options.OFFSET_BETWEEN_ENEMIES;

public class EnemiesController {

    private int numberOfEnemies = NUMBER_OF_ENEMIES;
    private float spawnPosition = 11f;
    private float offsetBetweenCars = OFFSET_BETWEEN_ENEMIES;
    private float enemiesSpeed = 0.08f;
    static int count=0;
    private ArrayList<TexCar> enemies;
    private TexCar lastSpawnedEnemy;
    private GL10 gl;

    private Random random;

    public EnemiesController(GL10 gll0) {

        gl = gll0;

        lastSpawnedEnemy = null;

        enemies = new ArrayList<TexCar>();

        random = new Random();
    }

    public void update(TexCar player) {

        if (enemies.size() == 0) {
            addCar();
        } else if (enemies.size() < numberOfEnemies) {

            if (spawnPosition - lastSpawnedEnemy.getPosition() >= offsetBetweenCars) {
                addCar();
            }
        }

        for (TexCar car : enemies) {

            respawn(car);

            updatePositions(car);
        }
    }

    private void respawn(TexCar car) {

        if (car.getPosition() <= -1) {
            count++;
            Log.i("count",""+ count);
            car.setTrack(getRandomTrack());
            car.setPosition(spawnPosition);
        }
    }

    private void addCar() {
        TexCar enemy = new TexCar(spawnPosition, getRandomTrack());

        enemy.loadTexture(gl, Global.ENEMY, Global.context);
        enemy.setSpeed(enemiesSpeed);

        enemies.add(enemy);

        lastSpawnedEnemy = enemy;


    }

    private int getRandomTrack() {
        return random.nextInt(3) + 2;
    }

    public boolean isAnyColliding(TexCar player) {

        Bounds playerBounds = player.getCurrentBounds();

        for (TexCar enemy : enemies) {

            Bounds enemyBounds = enemy.getCurrentBounds();

            if (playerBounds.Top >= enemyBounds.Down && playerBounds.Top <= enemyBounds.Top) {

                return checkRightOrLeftColliding(playerBounds, enemyBounds);
            } else if (playerBounds.Down >= enemyBounds.Down && playerBounds.Down <= enemyBounds.Top) {

                return checkRightOrLeftColliding(playerBounds, enemyBounds);
            }
        }
        return false;
    }

    private boolean checkRightOrLeftColliding(Bounds playerBounds, Bounds enemyBounds) {

        if ((playerBounds.Right <= enemyBounds.Right && playerBounds.Right >= enemyBounds.Left) ||
                (playerBounds.Left >= enemyBounds.Left && playerBounds.Left <= enemyBounds.Right)) {
            return true;
        }

        return false;
    }

    private void updatePositions(TexCar car) {

        car.setPosition(car.getPosition() - car.getSpeed());

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(.15f, Global.getProportionateHeight(0.15f), .15f);
        gl.glTranslatef(car.getTrack(), car.getPosition(), 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);

        car.draw(gl);
        gl.glPopMatrix();
        gl.glLoadIdentity();
    }


}