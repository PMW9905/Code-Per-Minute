package cpmPackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    Stage window;
    Scene gameScene;
    //Overview:
    //Application implements a MVC Architecture for UI management.
    //CSS is used for managing dark/light themes.
    //.txt files used to store player data, prompts, etc. (Eventually plan on converting to .json)
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            //File Manager:
            //Loads language prompt data according to the language & category selected by the user.
            //Provides random prompts (from current category) to GameController upon request.
            FileManager fileManager = new FileManager();
            //Player Manager:
            //Stores high scores of player in txt file. Loads upon launch, and writes upon closure.
            //Sends high scores to GameController upon request.
            PlayerManager playerManager = new PlayerManager();
            //Game Model:
            //Stores data related to UI as Property objects (Allows for listeners/bindings)
            //Accessed only by Game Controller.
            GameModel gameModel = new GameModel();
            //Game Controller:
            //Takes in gameModel, fileManager, and playerManager as arguments.
            //Acts as the middle man between all classes:
            //Answers requests by gameView for UI updates
            //Retrieves data from gameModel
            //Accesses PlayerManager upon test completion.
            GameController gameController = new GameController(gameModel, fileManager, playerManager);
            //Game View:
            //Creates and manages all of the UI elements.
            //Utilizes bindings, listeners, and properties to dynamically update UI.
            GameView gameView = new GameView(gameController, gameModel);

            gameScene = new Scene(gameView.rootAsParent(), 1000, 600);
            gameScene.getStylesheets().add("cssThemes/darkTheme.css");
            window = primaryStage;
            window.setScene(gameScene);
            window.show();

            //Upon closing out of application, thread for Timer is closed
            //Player Manager writes updated player data to playerData.txt
            window.setOnCloseRequest(e -> {
                gameController.closeAllThreads();
                playerManager.writePlayerData();
                Platform.exit();
                System.exit(0);
            });
        }
        catch(Exception e)
        {
            System.out.println("Exception caught whilst trying to construct application.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
