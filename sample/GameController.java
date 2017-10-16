package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GameController extends Application {


    String playerName = "Player";

    public void setPlayerName(String p){
        playerName = p;
    }

    public void gameLost(GameLoop gameLoop, ArrayList<Sprite> enemies, IntValue time_played, Stage gameStage) throws IOException{
        String time = String.valueOf(time_played.value);

        enemies.clear();
        //backgroundMusicPlayer.stop();
        ScoreboardFileManager scoreboardFile = new ScoreboardFileManager("src/scoreboard.txt");
        String[] scoreStringArray = {playerName,time};

        scoreboardFile.update(scoreStringArray);
        LoseScreen screen = new LoseScreen(time_played);
        screen.setPlayerName(playerName);
        gameLoop.stop();
        SceneController.setNewScene(screen, gameStage, this);
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
    public void start(Stage gameStage) throws Exception {

        gameStage.setTitle("Nikkern i Pikkern - Avoid getting hit!");


        Group root = new Group();
        Scene scene = new Scene(root);
        gameStage.setScene(scene);

        Canvas canvas = new Canvas(1024, 512);
        HBox hbox = new HBox();
        hbox.getChildren().add(canvas);
        root.getChildren().add(hbox);

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

        ArrayList<Sprite> enemies = makeEnemySprites(10, "cop.png");

        LongValue lastNanoTime = new LongValue(System.nanoTime());
        IntValue time_counter = new IntValue(0);

        GameLoop gameLoop = new GameLoop(this, gameStage, lastNanoTime,
                time_counter, health, player, time_played, enemies,
                canvas, input, gc);

        gameLoop.start();
        gameStage.show();

    }

}
