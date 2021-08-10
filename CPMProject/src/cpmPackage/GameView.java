package cpmPackage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameView {
    //Typing test elements
    private Label[] textPrompts; // 2: current; 1: next; 0: last;
    private TextField inputField;
    private VBox promptAndInputVBox;
    private BorderPane borderPane;
    private StackPane root;

    // Timer elements
    private Label timerLabel;
    private Rectangle timerRectangle;
    private StackPane timerStack;

    //Selection elements
    private ComboBox<String> languageSelect;
    private ComboBox<String> categorySelect;
    private ObservableList<String> languageOptions;
    private ObservableList<String> categoryOptions;
    private CheckBox themeCheckBox;
    private VBox selectBox;

    //Result-screen elements
    private Label resultsDisplay;
    private Label resultsLabel;
    private Rectangle resultsRectangle;
    private Button gameViewButton;
    private VBox resultsVBox;

    //Option organization
    private HBox optionHBox;


    private final GameController gameController;
    private final GameModel gameModel;

    public GameView(GameController gc, GameModel gm)
    {
        //Game Controller and Game Model object references are created.
        gameController = gc;
        gameModel = gm;

        constructPrompts();
        constructInputField();
        constructComboBoxes();
        constructThemeToggle();
        constructTimer();
        constructResultsScreen();
        constructRoot();

        createInputListeners();
        createComboBoxListeners();
        createCompletedGameListener();

        createPromptBindings();

        updateTextPrompts();

    }

    public Parent rootAsParent()
    {
        return root;
    }
    //Constructs the 3 Text Labels that display the upcoming text prompts.
    public void constructPrompts()
    {
        textPrompts = new Label[3];
        for(int i = 0; i<3; i++)
        {
            textPrompts[i] = new Label();
            textPrompts[i].setTextAlignment(TextAlignment.CENTER);
            textPrompts[i].setFont(new Font(40));
        }
        textPrompts[0].getStyleClass().add("last-prompt");
        textPrompts[1].getStyleClass().add("next-prompt");
        textPrompts[2].getStyleClass().add("current-prompt");
    }
    //Constructs the input field where the user will type their answers.
    public void constructInputField()
    {
        inputField = new TextField();
        inputField.setFont(new Font(40));
        inputField.setPrefWidth(600);
    }
    //Constructs the language and category combo boxes and VBox that house them.
    public void constructComboBoxes()
    {
        //Language dropdown menu.
        //Grabs list of languages from gameController.
        languageOptions = FXCollections.observableArrayList(gameController.getLanguages());
        languageSelect = new ComboBox<>(languageOptions);
        languageSelect.setValue("Java");
        languageSelect.setPrefSize(200,30);
        //Category dropdown menu.
        //Grabs list of categories from gameController.
        categoryOptions = FXCollections.observableArrayList(gameController.getCategories());
        categorySelect = new ComboBox<>(categoryOptions);
        categorySelect.setValue(gameController.getCurrentCategory());
        categorySelect.setPrefSize(200,30);
        //Labels that sit above dropdown menus.
        Label languageText = new Label("Language Select");
        Label categoryText = new Label("Category Select");
        //VBox that houses the language and category options.
        selectBox = new VBox();
        selectBox.getChildren().addAll(languageText,languageSelect,categoryText,categorySelect);
        selectBox.setAlignment(Pos.CENTER);
        selectBox.setSpacing(10);
    }
    //Constructs the checkbox that toggles dark-mode.
    public void constructThemeToggle()
    {
        themeCheckBox = new CheckBox("Toggle Dark-Theme");
        themeCheckBox.setSelected(true);//By default, dark-mode is enabled.
        //Upon toggle, the .css style sheet is swapped via a listener & function calls of gameController.
        themeCheckBox.selectedProperty().addListener((obs,oldSetting,newSetting)->
        {
            root.getScene().getStylesheets().remove(gameController.getCSSFile(oldSetting));
            root.getScene().getStylesheets().add(gameController.getCSSFile(newSetting));
            gameController.setIsDark(newSetting);
        });
        selectBox.getChildren().add(themeCheckBox);
    }
    //Constructs timer UI; not actual timer object/thread (that is handled by the Game Controller)
    public void constructTimer()
    {
        //Displays time remaining.
        timerLabel = new Label();
        timerLabel.textProperty().bind(gameModel.timerValueProperty());
        timerLabel.setFont(new Font(40));
        //Box that houses the timer.
        timerRectangle = new Rectangle(150,100);
        timerRectangle.getStyleClass().add("timer");
        //StackPane that stores both the timer label and the box.
        timerStack = new StackPane();
        timerStack.getChildren().addAll(timerRectangle,timerLabel);
        timerStack.setAlignment(Pos.CENTER);
    }
    //Constructs UI elements for results screen.
    public void constructResultsScreen()
    {
        //Results of test
        resultsDisplay = new Label("100");
        resultsDisplay.setFont(new Font(80));
        resultsDisplay.setTextAlignment(TextAlignment.CENTER);
        //Labeling of elements
        resultsLabel = new Label("ResultsText\nSecond Line");
        resultsLabel.setFont(new Font(20));
        resultsLabel.setTextAlignment(TextAlignment.CENTER);
        //Button that returns player to game screen
        gameViewButton = new Button("Return to Game");
        gameViewButton.getStyleClass().add("results-button");
        gameViewButton.setFont(new Font(20));
        //Rectangle that serves as the backdrop for the results screen.
        resultsRectangle = new Rectangle(300,400);
        resultsRectangle.setStrokeWidth(0);
        resultsRectangle.setStroke(Color.BLACK);
        resultsRectangle.getStyleClass().add("results-rectangle");
        //Contains UI elements for results.
        resultsVBox = new VBox();
        resultsVBox.getChildren().addAll(resultsDisplay,resultsLabel,gameViewButton);
        resultsVBox.setSpacing(20);
        resultsVBox.setAlignment(Pos.CENTER);
        //When button is pressed, the UI elements are pushed into the background so as not to be used/seen.
        gameViewButton.setOnAction((e)->
        {
            resultsRectangle.setStrokeWidth(0);
            resultsRectangle.toBack();
            resultsVBox.toBack();
        });
    }
    //Constructs root element that holds all UI elements
    //This is what is sent to Main Application.
    public void constructRoot()
    {
        //Houses prompt and input elements.
        promptAndInputVBox = new VBox();
        promptAndInputVBox.setAlignment(Pos.BOTTOM_CENTER);
        promptAndInputVBox.setSpacing(20);
        promptAndInputVBox.getChildren().addAll(textPrompts);
        promptAndInputVBox.getChildren().add(inputField);
        //Houses the timer and option elements.
        optionHBox = new HBox();
        optionHBox.getChildren().addAll(timerStack,selectBox);
        optionHBox.setPadding(new Insets(10,10,10,10));
        optionHBox.setAlignment(Pos.CENTER);
        optionHBox.setSpacing(500);
        //Holds optionHBox and promptAndInputVbox & formats them
        borderPane = new BorderPane();
        borderPane.setCenter(promptAndInputVBox);
        borderPane.setBottom(optionHBox);//replace with select box if doesn't work
        //Holds all UI elements.
        root = new StackPane();
        root.getChildren().addAll(resultsVBox,resultsRectangle,borderPane);
    }
    //Established listeners for both the input field and the shouldClearProperty in gameModel.
    public void createInputListeners()
    {
        //Determines when timer should start.
        //Also determines what color text should be (red if incorrect, black if correct)
        inputField.textProperty().addListener((obs,oldUserInput,newUserInput)->
        {
            if(inputField.textProperty().get()!=null && !gameController.getShouldClear())
                gameController.shouldStartTest();
            textPrompts[2].setTextFill(Paint.valueOf(gameController.compareInputToPrompt(newUserInput)));
        });
        //Clears text prompt if shouldClear is true.
        gameModel.shouldClearProperty().addListener((obs,oldBool,newBool)->{
            if(newBool)
                Platform.runLater(() -> {
                    inputField.textProperty().set("");
                    gameController.setShouldClear(false);
                });
        });
    }
    //Creates listeners for the language/category options.
    public void createComboBoxListeners()
    {
        //Category drop down menu. Loads in new category from the FileManager's allCategories Map.
        categorySelect.getSelectionModel().selectedItemProperty().addListener((obs,oldItem,newItem)->
        {
            if(newItem!=null)
                gameController.configureNewCategory(newItem);
            Platform.runLater(() ->
            {
                gameController.setShouldClear(true);
                gameController.endTest();
            });
        });
        //Language drop down menu. Reads corresponding language file and stores data in FileManager's allCategories map.
        languageSelect.getSelectionModel().selectedItemProperty().addListener((obs,oldItem,newItem)->
        {
            gameController.configureNewLanguage(newItem);
            categoryOptions = FXCollections.observableArrayList(gameController.getCategories());
            categorySelect.setItems(categoryOptions);
            categorySelect.setValue(gameController.getCurrentCategory());
            Platform.runLater(() ->
            {
                gameController.setShouldClear(true);
                gameController.endTest();
            });
        });
    }
    //Listens to shouldDisplayResultsProperty to see whether game should end.
    //Value set to true when timer completes.
    //Brings up game menu
    public void createCompletedGameListener()
    {
        gameModel.shouldDisplayResultsProperty().addListener((obs,oldVal,newVal)->
        {
            if(newVal)
            {
                //Runs on platform.runlater so that it can access and manipulate UI elements.
                Platform.runLater(() ->
                {
                    //Deselects textbox
                    inputField.deselect();
                    gameViewButton.requestFocus();
                    gameController.clearResultsBool();
                    //Configures text of results screen
                    resultsDisplay.setText(""+gameController.getCurrentScore());
                    resultsLabel.setText("CPM\n\n High Score: "+gameController.getHighScore());
                    //Brings result screen to the front.
                    resultsRectangle.setStrokeWidth(2);
                    resultsRectangle.toFront();
                    resultsVBox.toFront();
                });
            }
        });
    }
    //Binds text prompts to gameModel properties so that they update automatically.
    public void createPromptBindings()
    {
        textPrompts[0].textProperty().bind(gameModel.lastPromptProperty());
        textPrompts[1].textProperty().bind(gameModel.nextPromptProperty());
        textPrompts[2].textProperty().bind(gameModel.currentPromptProperty());
    }
    //Refreshes text on text-prompts.
    //Used when switching languages/categories.
    public void updateTextPrompts()
    {
        gameController.configureNewCategory(gameController.getCurrentCategory());
    }
}
