package cpmPackage;

import javafx.beans.property.*;

public class GameModel {
    private final StringProperty currentPrompt = new SimpleStringProperty();
    private final StringProperty nextPrompt = new SimpleStringProperty();
    private final StringProperty lastPrompt = new SimpleStringProperty();
    private final StringProperty userInput = new SimpleStringProperty();

    private final StringProperty timerValue = new SimpleStringProperty();

    private final IntegerProperty currentScore = new SimpleIntegerProperty();
    private final IntegerProperty highScore = new SimpleIntegerProperty();

    //Booleans that are listened to from GameView.

    //Stores if user input box should be cleared
    private final BooleanProperty shouldClear = new SimpleBooleanProperty();
    //Stores whether darkTheme or Light theme are enabled.
    private final BooleanProperty isDarkTheme = new SimpleBooleanProperty();
    //Stores if the timer is still running/if the player is still testing.
    private final BooleanProperty isTesting = new SimpleBooleanProperty();
    //Is true if the results screen should be shown.
    private final BooleanProperty shouldDisplayResults = new SimpleBooleanProperty();

    public GameModel()
    {
        shouldClear.set(false);
        isDarkTheme.set(true);
        isTesting.set(false);
        shouldDisplayResults.set(false);
        timerValue.set("1:00");
    }
    //gsp for currentPrompt
    public final String getCurrentPrompt(){return currentPrompt.get();}
    public final void setCurrentPrompt(final String s){currentPrompt.set(s);}
    public final StringProperty currentPromptProperty(){return currentPrompt;}
    //gsp for nextPrompt
    public final String getNextPrompt(){return nextPrompt.get();}
    public final void setNextPrompt(final String s){nextPrompt.set(s);}
    public final StringProperty nextPromptProperty(){return nextPrompt;}
    //gsp for lastPrompt
    public final String getLastPrompt(){return lastPrompt.get();}
    public final void setLastPrompt(final String s){lastPrompt.set(s);}
    public final StringProperty lastPromptProperty(){return lastPrompt;}
    //gsp for userInput
    public final String getUserInput(){return userInput.get();}
    public final void setUserInput(final String s){userInput.set(s);}
    public final StringProperty userInputProperty(){return userInput;}

    //gsp for timerValue
    public final String getTimerValue(){return timerValue.get();}
    public final void setTimerValue(final String s){timerValue.set(s);}
    public final StringProperty timerValueProperty(){return timerValue;}

    //gsp for currentScore
    public final int getCurrentScore(){return currentScore.get();}
    public final void setCurrentScore(int n){currentScore.set(n);}
    public final IntegerProperty currentScoreProperty(){return currentScore;}
    //gsp for highScore
    public final int getHighScore(){return highScore.get();}
    public final void setHighScore(int n){highScore.set(n);}
    public final IntegerProperty highScoreProperty(){return highScore;}


    //gsp for shouldClear
    public final boolean getShouldClear(){return shouldClear.get();}
    public final void setShouldClear(final boolean b){shouldClear.set(b);}
    public final BooleanProperty shouldClearProperty(){return shouldClear;}
    //gsp for isDarkTheme
    public final boolean getIsDarkTheme(){return isDarkTheme.get();}
    public final void setIsDarkTheme(final boolean b){isDarkTheme.set(b);}
    public final BooleanProperty isDarkThemeProperty(){return isDarkTheme;}
    //gsp for isTesting
    public final boolean getIsTesting(){return isTesting.get();}
    public final void setIsTesting(final boolean b){isTesting.set(b);}
    public final BooleanProperty isTestingProperty(){return isTesting;}
    //gsp for shouldDisplayResults
    public final boolean getShouldDisplayResults(){return shouldDisplayResults.get();}
    public final void setShouldDisplayResults(final boolean b){shouldDisplayResults.set(b);}
    public final BooleanProperty shouldDisplayResultsProperty(){return shouldDisplayResults;}

}
