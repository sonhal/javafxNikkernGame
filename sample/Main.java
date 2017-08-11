package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {
    Main main_obj = this;

    GameMusicplayer backgroundMusicPlayer = new GameMusicplayer("Dynamic-good-electronic-music.mp3");


    public void gameLost(ArrayList<Sprite> enemies, IntValue time_played, Stage gameStage){
        enemies.clear();
        LoseScreen screen = new LoseScreen(time_played, main_obj);
        try {
            gameStage.hide();
            screen.start(gameStage);
            stop();
            //backgroundMusicPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Sprite> makeEnemySprites(int amount, String spriteImage){

        ArrayList<Sprite> enemies = new ArrayList<Sprite>();

        for(int i = 0; i < amount; i++) {

            Sprite enemy = new Sprite();
            enemy.setImage(spriteImage);
            double px = 450 * Math.random() + 300;
            double py = 350 * Math.random() + 50;
            enemy.setPosition(px,py);
            enemies.add(enemy);
        }
        return  enemies;
    }


    @Override
    public void start(Stage gameStage) throws Exception{
        backgroundMusicPlayer.setAutoRepeat();
        backgroundMusicPlayer.toggleAutoPlay(true);





        gameStage.setTitle("Nikkern i Pikkern - Avoid getting hit!");


        Group root = new Group();
        Scene scene = new Scene(root);
        gameStage.setScene(scene);

        Canvas canvas = new Canvas(1024, 512);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font font = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(font);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Sprite player = new Sprite();
        player.setImage("player.png");
        player.setPosition(200, 50);
        IntValue health = new IntValue(5);
        ArrayList<String> input = new ArrayList<String>();


        IntValue time_played = new IntValue(0);






        scene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        String code = event.getCode().toString();
                        if (!input.contains(code)) {
                            input.add(code);
                        }
                    }
                }
        );

        scene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        String code = event.getCode().toString();
                        input.remove(code);
                    }
                }
        );

        ArrayList<Sprite> enemies = makeEnemySprites(15,"cop.png");

        LongValue lastNanoTime = new LongValue(System.nanoTime());
        IntValue time_counter = new IntValue(0);

        new AnimationTimer() {
            int counter = 0;
            boolean hit = false;


            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                if(time_counter.value >= 50 && health.value > 0){
                    time_played.value++;
                    time_counter.value = 0;
                }

                time_counter.value++;



                if(counter == 0 && hit){
                    player.setImage("player.png");
                    hit = false;
                }
                if(hit == true){
                    counter--;
                }

                if(health.value < 1){
                    gameLost(enemies,time_played,gameStage);
                }


                if (player.getPositionX() > canvas.getWidth()) {
                    player.setVelocityX(-10);

                }
                else if (player.getPositionY() > canvas.getHeight()) {
                    player.setVelocityY(-10);
                }
                else if (player.getPositionX() < 0) {
                    player.setVelocityX(10);
                }
                else if (player.getPositionY() < 0) {
                    player.setVelocityY(10);
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

                Iterator<Sprite> enemyIter = enemies.iterator();
                Iterator<Sprite> enemyIter2 = enemies.iterator();
                while (enemyIter.hasNext()) {

                    boolean bump_lock = false;
                    Sprite enemy = enemyIter.next();
                    if (player.intersects(enemy)) {
                        enemyIter.remove();
                        health.value--;
                        player.setImage("player_hit.png");
                        hit = true;
                        counter = 10;
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


                    if (enemy.getPositionX() > canvas.getWidth()) {
                        enemy.setVelocityX(-50);
                        bump_lock = true;
                    }
                    if (enemy.getPositionY() > canvas.getHeight()) {
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
        }.start();


        gameStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
