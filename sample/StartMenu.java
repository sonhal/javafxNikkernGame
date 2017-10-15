package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class StartMenu extends Application {

StartMenu baseObj = this;
String playername = "Player";

    public void setPlayername(String p){
        playername = p;
    }

    public void getPlayername(String p){
        playername = p;
    }

    public void startNewGame(Stage gameStage){
        GameController game = new GameController();
        game.setPlayerName(playername);
        try {
            gameStage.hide();
            game.start(gameStage);
            stop();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("NIkkern i Pikkern - Avoid getting hit!");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label text = new Label("Game objectives:");
        Text texts = new Text("Stay alive as long as possible! \nDont let the police get you!\n");
        Label header = new Label("Nikkern i Pikkern the Game");
        Label playerNameLabel = new Label(playername);
        Button playButton = new Button("Play");
        Button scoreboardButton = new Button("Scoreboard");
        texts.setTextAlignment(TextAlignment.CENTER);
        text.setAlignment(Pos.CENTER);
        playerNameLabel.setTextAlignment(TextAlignment.CENTER);


        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        Image losing_image = new Image("player_lose.png");
        ImageView view = new ImageView();
        view.setImage(losing_image);
        //grid.setGridLinesVisible(true);


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(view);

        HBox hBoxPlayerLabel = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(playerNameLabel);

        HBox hBoxLahel = new HBox();
        hBoxLahel.setAlignment(Pos.CENTER);
        hBoxLahel.getChildren().add(text);


        HBox hBoxText = new HBox();
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.getChildren().add(texts);

        HBox hBoxStart = new HBox();
        hBoxStart.setAlignment(Pos.CENTER);
        hBoxStart.getChildren().add(playButton);

        HBox hBoxScoreboard = new HBox();
        hBoxScoreboard.setAlignment(Pos.CENTER);
        hBoxScoreboard.getChildren().add(scoreboardButton);


        grid.add(hBoxText, 0, 4);
        grid.add(hBoxLahel, 0, 3);
        grid.add(hBoxPlayerLabel, 0, 1);
        grid.add(hBox, 0,2);
        grid.add(header, 0,0);
        grid.add(hBoxStart, 0,5);
        grid.add(hBoxScoreboard, 0,6);


        Scene lose_scene = new Scene(grid, 400,400);
        primaryStage.setScene(lose_scene);
        primaryStage.show();

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startNewGame(primaryStage);

            }
        });

        //Switch to scoreboard
        scoreboardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scoreboard scoreboard = new Scoreboard();
                scoreboard.setPlayerName(playername);
                SceneController.setNewScene(scoreboard,primaryStage,baseObj);

            }
        });


    }
}
