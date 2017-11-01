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

    public void checkBoundariesAndKeypress(Sprite sprite, boolean isPlayer){
        if (sprite.getPositionX() < 10) {
            sprite.setPosition(0,sprite.getPositionY());
        }
        if (sprite.getPositionY() > canvas.getHeight() - 82) {
            sprite.setPosition(sprite.getPositionX(),canvas.getHeight() - 92);
        }
        if (sprite.getPositionX() > (canvas.getWidth() - 82) ) {
            sprite.setPosition(canvas.getWidth() - 92, sprite.getPositionY());
        }
        if (sprite.getPositionY() < 0) {
            sprite.setPosition(sprite.getPositionX(),5);
        }

        if(player.getPositionY() < 0 && player.getPositionX() < 0){
            player.setPosition(10,10);
        }
        if(player.getPositionY() > canvas.getHeight() - 80 && player.getPositionX() > canvas.getWidth() - 80){
            player.setPosition(canvas.getWidth() - 90,canvas.getHeight() - 90);
        }
        if(player.getPositionY() > canvas.getHeight() - 80 && player.getPositionX() < 0){
            player.setPosition(10,canvas.getHeight() - 90);
        }
        if(player.getPositionY() < 0 && player.getPositionX() > canvas.getWidth() - 80){
            player.setPosition(canvas.getWidth() - 90,10);
        }
        else{
            if(isPlayer){
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
        }
    }

    public boolean checkEnemyCollision(Sprite enemy) {
        boolean bump_lock = false;
        for (Sprite enemy2 : enemies) {
            //Sprite enemy2 = enemyIter2.next();
            if (enemy2 == enemy) {
                System.out.println("pass");
                continue;
            }
            if (enemy.circleIntersects(enemy2)) {
                System.out.println("HIT------------------------");
                enemy.calcCollisionBounce(enemy2);
                bump_lock = true;
                enemy.update(.01);
                enemy2.update(.01);
            }
        }
        return bump_lock;
    }

    public void checkEnemyToPlayerCollision(Sprite enemy){
        if (player.circleIntersects(enemy)) {
            //enemyIter.remove();
            enemy.calcCollisionBounceWithPlayer(player);
            enemy.update(.01);
            player.update(.01);
            if(invinsible == false){
                //health.value--;
                invinsible = true;
                /*Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Sprite newEnemy = new Sprite();
                        newEnemy.setImage("cop.png");
                        newEnemy.setPosition(enemy.getPositionX() + 100,enemy.getPositionY() - 100);
                        enemies.add(newEnemy);
                    }
                });*/
                player.setImage("player_hit.png");
                hit = true;
                counter = 30;
                GameMusicplayer hitMusicPlayer = new GameMusicplayer("Cannon-sound-effect.mp3");
                hitMusicPlayer.play();
            }
        }
    }

    public void generateRandomMovmentIfNoCollision(Sprite enemy, boolean bump_lock){
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
    }

    public boolean checkEnemyBoundaryCollision(Sprite enemy,boolean bump_lock){
        if (enemy.getPositionX() > canvas.getWidth() - 80) {
            enemy.setPosition(canvas.getWidth() - 90,enemy.getPositionY());
            enemy.setVelocityX(-50);
            bump_lock = true;
        }
        if (enemy.getPositionY() > canvas.getHeight() - 90) {
            enemy.setPosition(enemy.getPositionX(), canvas.getHeight() - 100);
            enemy.setVelocityY(-50);
            bump_lock = true;
        }
        if (enemy.getPositionX() < 0) {
            enemy.setPosition(10,enemy.getPositionY());
            enemy.setVelocityX(50);
            bump_lock = true;
        }
        if (enemy.getPositionY() < 0) {
            enemy.setPosition(enemy.getPositionX(),10);
            enemy.setVelocityY(50);
            bump_lock = true;
        }
        if(enemy.getPositionY() < 0 && enemy.getPositionX() < 0){
            enemy.setPosition(10,10);
            enemy.setVelocityY(50);
            enemy.setVelocityX(50);
            bump_lock = true;
        }
        if(enemy.getPositionY() > canvas.getHeight() - 80 && enemy.getPositionX() > canvas.getWidth() - 80){
            enemy.setPosition(canvas.getWidth() - 90,canvas.getHeight() - 90);
            enemy.setVelocityY(-50);
            enemy.setVelocityX(-50);
            bump_lock = true;
        }
        if(enemy.getPositionY() > canvas.getHeight() - 80 && enemy.getPositionX() < 0){
            enemy.setPosition(10,canvas.getHeight() - 90);
            enemy.setVelocityY(-50);
            enemy.setVelocityX(50);
            bump_lock = true;
        }
        if(enemy.getPositionY() < 0 && enemy.getPositionX() > canvas.getWidth() - 80){
            enemy.setPosition(canvas.getWidth() - 90,10);
            enemy.setVelocityY(50);
            enemy.setVelocityX(-50);
            bump_lock = true;
        }
        return bump_lock;
    }


    public void checkEnemies(Iterator<Sprite> enemyIter){
        while (enemyIter.hasNext()) {
            boolean bump_lock = false;
            Sprite enemy = enemyIter.next();

            //Check if enemy has collided with another enemy
            //Sets bump_lock to true if so




            bump_lock = checkEnemyBoundaryCollision(enemy,bump_lock);

            bump_lock = checkEnemyCollision(enemy);

            generateRandomMovmentIfNoCollision(enemy,bump_lock);

            checkEnemyToPlayerCollision(enemy);

        }
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



        checkBoundariesAndKeypress(player,true);

        checkEnemies(enemyIter);



        player.update(elapsedTime);
        for(Sprite enemy : enemies){
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

