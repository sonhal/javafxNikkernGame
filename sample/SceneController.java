package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class SceneController {

   static final double SCENE_WIDTH = 1024;
   static final double SCENE_HEIGHT = 512;



    public static void setNewScene(Application newScene, Stage stage, Application oldScene){

        try {
            stage.hide();
            newScene.start(stage);
            oldScene.stop();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
