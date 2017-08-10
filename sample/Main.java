package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {

    @Override
    public void start(Stage gameStage) throws Exception{

        gameStage.setTitle("Avoid getting hit!");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label text = new Label("You lose!");
        Label header = new Label("Niklas died!");
        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        Image losing_image = new Image("player_lose.png");
        ImageView view = new ImageView();
        view.setImage(losing_image);


        grid.add(text, 1, 2);
        grid.add(view, 1,1);
        grid.add(header, 1,0);


        Scene lose_scene = new Scene(grid, 300,300);


        Group root = new Group();
        Scene scene = new Scene(root);
        gameStage.setScene(scene);

        Canvas canvas = new Canvas(1024, 512);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

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

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font font = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(font);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Sprite player = new Sprite();
        player.setImage("player.png");
        player.setPosition(200, 0);


        ArrayList<Sprite> enemies = new ArrayList<Sprite>();


        for(int i = 0; i < 15; i++) {

            Sprite enemy = new Sprite();
            enemy.setImage("cop.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;
            enemy.setPosition(px,py);
            enemies.add(enemy);
        }

        LongValue lastNanoTime = new LongValue(System.nanoTime());

        IntValue health = new IntValue(5);



        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                if(health.value < 1){

                    gameStage.setScene(lose_scene);
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
                        player.addVelocity(-150, 0);
                    if (input.contains("RIGHT"))
                        player.addVelocity(150, 0);
                    if (input.contains("UP"))
                        player.addVelocity(0, -150);
                    if (input.contains("DOWN"))
                        player.addVelocity(0, 150);

                }

                player.update(elapsedTime);

                Iterator<Sprite> enemyIter = enemies.iterator();
                while (enemyIter.hasNext()) {

                    boolean bump_lock = false;
                    Sprite enemy = enemyIter.next();
                    if (player.intersects(enemy)) {
                        enemyIter.remove();
                        health.value--;
                    }


                    if (enemy.getPositionX() > canvas.getWidth()) {
                        enemy.setVelocityX(-10);
                        bump_lock = true;
                    }
                    if (enemy.getPositionY() > canvas.getHeight()) {
                        enemy.setVelocityY(-10);
                        bump_lock = true;
                    }
                    if (enemy.getPositionX() < 0) {
                        enemy.setVelocityX(10);
                        bump_lock = true;
                    }
                    if (enemy.getPositionY() < 0) {
                        enemy.setVelocityY(10);
                        bump_lock = true;
                    }

                    if(bump_lock == false){
                        double chance = Math.random() * 10;
                        double chance2 = Math.random() * 10;


                        if(chance > 5){

                            if(chance2 > 5) {
                                enemy.addVelocity(5,5);
                            }
                            else {
                                enemy.addVelocity(-5,5);
                            }


                        }
                        else {
                            if(chance2 < 5) {
                                enemy.addVelocity(5,-5);
                            }
                            else {
                                enemy.addVelocity(-5,-5);
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

            }
        }.start();


        gameStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
