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
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Scoreboard extends Application {
    ScoreboardFileManager scoreboardFile;
    Scoreboard thisScoreboard = this;
    String playerName = "Player";

    public Scoreboard(){
        scoreboardFile = new ScoreboardFileManager("src/scoreboard.txt");
    }

    public void setPlayerName(String p) {
        this.playerName = p;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String[] s = {"SonGetHal", "100"};
        //scoreboardFile.update(s);
        String scoreboard = scoreboardFile.getFormatedScoreboard();

        primaryStage.setTitle("NIkkern i Pikkern - Avoid getting hit!");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text texts = new Text(scoreboard);
        Label header = new Label("Scoreboard");
        Button playButton = new Button("Menu");
        texts.setTextAlignment(TextAlignment.CENTER);



        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        Image losing_image = new Image("player_lose.png");
        ImageView view = new ImageView();
        view.setImage(losing_image);
        //grid.setGridLinesVisible(true);


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(view);

        HBox hBoxLahel = new HBox();
        hBoxLahel.setAlignment(Pos.CENTER);



        HBox hBoxText = new HBox();
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.getChildren().add(texts);

        HBox hBoxStart = new HBox();
        hBoxStart.setAlignment(Pos.CENTER);
        hBoxStart.getChildren().add(playButton);


        grid.add(hBoxText, 0, 4);
        grid.add(hBoxLahel, 0, 3);
        grid.add(hBox, 0,2);
        grid.add(header, 0,0);
        grid.add(hBoxStart, 0,5);

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartMenu menu = new StartMenu();
                menu.setPlayername(playerName);
                SceneController.setNewScene(menu, primaryStage, thisScoreboard);

            }
        });

        Scene lose_scene = new Scene(grid, 400,400);
        primaryStage.setScene(lose_scene);
        primaryStage.show();

    }
}
