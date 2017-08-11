package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {


    public void setup_game(Stage gameStage, Scene scene, Sprite player, ArrayList<Sprite> enemies, IntValue health, ArrayList<String> input, IntValue time){


        health.value = 5;

        player.setPosition(200, 100);
        player.setVelocity(0,0);
        input.clear();



        enemies.clear();


        for(int i = 0; i < 15; i++) {

            Sprite enemy = new Sprite();
            enemy.setImage("cop.png");
            double px = 350 * Math.random() + 300;
            double py = 350 * Math.random() + 50;
            enemy.setPosition(px,py);
            enemies.add(enemy);
        }
        time.value = 0;

        gameStage.setScene(scene);


    }

    public void play_hit_sound(){
        String musicFileHit = "Cannon-sound-effect.mp3";     // For example

        Media sound_hit = new Media(new File(musicFileHit).toURI().toString());
        MediaPlayer mediaPlayerHit = new MediaPlayer(sound_hit);
        mediaPlayerHit.play();
    }

    @Override
    public void start(Stage gameStage) throws Exception{


        String musicFile = "Dynamic-good-electronic-music.mp3";

        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);





        gameStage.setTitle("NIkkern i Pikkern - Avoid getting hit!");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label text = new Label("You lose!");
        Label header = new Label("Niklas died!");
        Button replay_button = new Button("Play again");
        Label time_label = new Label("Time = 0");


        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        Image losing_image = new Image("player_lose.png");
        ImageView view = new ImageView();
        view.setImage(losing_image);


        grid.add(text, 1, 2);
        grid.add(view, 1,1);
        grid.add(header, 1,0);
        grid.add(replay_button, 1,3);
        grid.add(time_label, 1,4);


        Scene lose_scene = new Scene(grid, 300,300);


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


        ArrayList<Sprite> enemies = new ArrayList<Sprite>();

        IntValue time_played = new IntValue(0);

        replay_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                setup_game(gameStage,scene, player, enemies, health,input,time_played);

            }
        });




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




        for(int i = 0; i < 15; i++) {

            Sprite enemy = new Sprite();
            enemy.setImage("cop.png");
            double px = 450 * Math.random() + 300;
            double py = 350 * Math.random() + 50;
            enemy.setPosition(px,py);
            enemies.add(enemy);
        }

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
                    time_label.setText("Time: " + time_played.value + " seconds");
                    gameStage.setScene(lose_scene);
                    enemies.clear();

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
                        player.addVelocity(-250, 0);
                    if (input.contains("RIGHT"))
                        player.addVelocity(250, 0);
                    if (input.contains("UP"))
                        player.addVelocity(0, -250);
                    if (input.contains("DOWN"))
                        player.addVelocity(0, 250);

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
                        play_hit_sound();
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
