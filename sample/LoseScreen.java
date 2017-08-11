package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.security.PublicKey;

public class LoseScreen extends Application {
    IntValue time;
    GameController main;
    LoseScreen thisLoseScreen = this;


    public LoseScreen(IntValue time, GameController main){
        this.time = time;
        this.main = main;
    }


    @Override
    public void start(Stage gameStage) throws Exception {


        gameStage.setTitle("NIkkern i Pikkern - Avoid getting hit!");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label text = new Label("You lose!");
        Label header = new Label("Niklas died!");
        Button replay_button = new Button("Play again");
        Button menu_button = new Button("Menu");
        Label time_label = new Label("Time = " + time.value);


        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        Image losing_image = new Image("player_lose.png");
        ImageView view = new ImageView();
        view.setImage(losing_image);


        grid.add(menu_button, 1, 2);
        grid.add(view, 1,1);
        grid.add(header, 1,0);
        grid.add(replay_button, 1,3);
        grid.add(time_label, 1,4);


        Scene lose_scene = new Scene(grid, 300,300);
        gameStage.setScene(lose_scene);
        gameStage.show();

        replay_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneController.setNewScene(main, gameStage, thisLoseScreen);

            }
        });

        menu_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneController.setNewScene(new StartMenu(), gameStage, thisLoseScreen);

            }
        });
    }
}
