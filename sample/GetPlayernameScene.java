package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

public class GetPlayernameScene extends Application {

    GetPlayernameScene baseObj = this;

    public void startNewGame(Stage gameStage){
        GameController game = new GameController();

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

        Label header = new Label("Nikkern i Pikkern the Game");
        Button nextButton = new Button("Next");
        TextField nameInput = new TextField();

        grid.setGridLinesVisible(true);



        HBox hBoxText = new HBox();
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.getChildren().add(nameInput);

        HBox hBoxNext = new HBox();
        hBoxNext.setAlignment(Pos.CENTER);
        hBoxNext.getChildren().add(nextButton);


        grid.add(hBoxText, 0, 4);
        grid.add(header, 0,0);
        grid.add(hBoxNext, 0,5);



        Scene lose_scene = new Scene(grid, 400,400);
        primaryStage.setScene(lose_scene);
        primaryStage.show();


        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StartMenu startMenu = new StartMenu();
                startMenu.setPlayername(nameInput.getText());
                SceneController.setNewScene(startMenu,primaryStage,baseObj);

            }
        });


    }
}
