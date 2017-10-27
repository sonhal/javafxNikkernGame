package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GameLoop extends AnimationTimer {

    int x,y;
    boolean invinsible = false;
    IntValue time_played;
    Sprite player;
    GameController mainObject;
    LongValue lastNanoTime;
    IntValue time_counter;
    IntValue health;
    int counter = 0;
    boolean hit = false;
    ArrayList<Sprite> enemies;
    Stage gameStage;
    Canvas canvas;
    ArrayList<String> input;
    GraphicsContext gc;

    public GameLoop(GameController mainObject, Stage gameStage, LongValue lastNanoTime,
                    IntValue time_counter, IntValue health, Sprite player,
                    IntValue time_played, ArrayList<Sprite> enemies,
                    Canvas canvas, ArrayList<String> input,
                    GraphicsContext gc) {

        this.lastNanoTime = lastNanoTime;
        this.time_counter = time_counter;
        this.health = health;
        this.mainObject = mainObject;
        this.player = player;
        this.time_played = time_played;
        this.enemies = enemies;
        this.gameStage = gameStage;
        this.canvas = canvas;
        this.input = input;
        this.gc = gc;

    }






    public void handle(long currentNanoTime) {
        Iterator<Sprite> enemyIter = enemies.iterator();
        Iterator<Sprite> enemyIter2 = enemies.iterator();

        double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
        lastNanoTime.value = currentNanoTime;

        if(time_counter.value >= 50 && health.value > 0){
            time_played.value++;
            time_counter.value = 0;
        }

        time_counter.value++;



        if(counter == 10 && hit){
            player.setImage("player.png");
            hit = false;
        }
        if(counter == 0){
            invinsible = false;
        }

        if(hit == true || invinsible == true){
            counter--;
        }

        if(health.value < 1){
            try {
                mainObject.gameLost(this,enemies ,time_played, gameStage);
            }catch (IOException e){
                e.printStackTrace();
            }

        }


        if (player.getPositionX() < 0) {
            player.setVelocityX(50);

        }
        else if (player.getPositionY() > canvas.getHeight() - 82) {
            player.setVelocityY(-50);
        }
        else if (player.getPositionX() > (canvas.getWidth() - 82) ) {
            player.setVelocityX(-50);
        }
        else if (player.getPositionY() < 0) {
            player.setVelocityY(50);
        }
        else {

            player.setVelocity(0, 0);



            if (input.contains("LEFT"))
                player.addVelocity(-550, 0);
            if (input.contains("RIGHT"))
                player.addVelocity(550, 0);
            if (input.contains("UP"))
                player.addVelocity(0, -550);
            if (input.contains("DOWN"))
                player.addVelocity(0, 550);

        }

        player.update(elapsedTime);


        while (enemyIter.hasNext()) {

            boolean bump_lock = false;
            Sprite enemy = enemyIter.next();
            if (player.intersects(enemy) && invinsible == false) {
                invinsible = true;
                //enemyIter.remove();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Sprite newEnemy = new Sprite();
                        newEnemy.setImage("cop.png");
                        newEnemy.setPosition(enemy.getPositionX(),enemy.getPositionY());
                        enemies.add(newEnemy);
                    }
                });
                health.value--;
                player.setImage("player_hit.png");
                hit = true;
                counter = 20;
                GameMusicplayer hitMusicPlayer = new GameMusicplayer("Cannon-sound-effect.mp3");
                hitMusicPlayer.play();
            }

                   /* while (enemyIter.hasNext()){
                        if(enemyIter.next() == enemy){
                            break;
                        }
                        else {
                            if(enemy.intersects(enemyIter.next())){
                                double new_x = enemy.getVelocityX() * -1 + 5;
                                double new_y = enemy.getVelocityY() * -1 + 5;
                                double x = Math.random() * 10;
                                double y = Math.random() * 10;
                                enemyIter.next().setPosition(x,y);
                                enemy.setVelocity(new_x , new_y);
                                bump_lock = true;
                            }
                        }

                    } */


            if (enemy.getPositionX() > canvas.getWidth() - 80) {
                enemy.setVelocityX(-50);
                bump_lock = true;
            }
            if (enemy.getPositionY() > canvas.getHeight() - 80) {
                enemy.setVelocityY(-50);
                bump_lock = true;
            }
            if (enemy.getPositionX() < 0) {
                enemy.setVelocityX(50);
                bump_lock = true;
            }
            if (enemy.getPositionY() < 0) {
                enemy.setVelocityY(50);
                bump_lock = true;
            }

            if(bump_lock == false){
                double chance = Math.random() * 10;
                double chance2 = Math.random() * 10;


                if(chance > 5){

                    if(chance2 > 5) {
                        enemy.addVelocity(30,30);
                    }
                    else {
                        enemy.addVelocity(-30,30);
                    }


                }
                else {
                    if(chance2 < 5) {
                        enemy.addVelocity(30,-30);
                    }
                    else {
                        enemy.addVelocity(-30,-30);
                    }
                }


            }
            enemy.update(elapsedTime);



        }





        gc.clearRect(0,0,1024,512);
        player.render(gc);

        for (Sprite enemy : enemies) {
            enemy.render(gc);
        }

        String pointsText = "Health: " + health.value;
        gc.fillText(pointsText,360, 36);
        gc.strokeText(pointsText,360,36);

        String timeText = "Time: " + time_played.value;
        gc.fillText(timeText,560, 36);
        gc.strokeText(timeText,560,36);

    }
}

