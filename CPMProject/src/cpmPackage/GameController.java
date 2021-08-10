package cpmPackage;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private final GameModel gameModel;
    private final FileManager fileManager;
    private final PlayerManager playerManager;
    //Timer and TimerTask for typing test gameTimer
    private final Timer gameTimer;
    private TimerTask gameTimerTask;
    public GameController(GameModel gm,FileManager fm,PlayerManager pm)
    {
        gameModel = gm;
        fileManager = fm;
        playerManager = pm;
        gameTimer = new Timer();
        createGameTimerTask();
    }
    //Functionality of in-game timer.
    //Only triggers completeTest upon timer reaching 0
    public void createGameTimerTask()
    {
        gameTimerTask = new TimerTask() {
            int timeLeft=60;
            @Override
            public void run() {
                if(timeLeft>0)
                {
                    Platform.runLater(()->{
                        setTimerValue(timeLeft);
                        timeLeft--;
                    });
                }
                else
                {
                    Platform.runLater(()->{
                        setTimerValue(timeLeft);
                        completeTest();
                    });
                }
            }
        };
    }

    //Retrieves list of languages/categories from fileManager
    public ArrayList<String> getLanguages(){return fileManager.getAllLanguages();}
    public ArrayList<String> getCategories(){return fileManager.getAllCategories();}

    //Returns current category from fileManager
    public String getCurrentCategory(){return fileManager.getCurrentCategory();}

    //Run by GameView. Upon selecting aa new category, fileManager sets new category.
    //3 Random prompts are selected.
    public void configureNewCategory(String s)
    {
        fileManager.setCurrentCategory(s);
        gameModel.setCurrentPrompt(fileManager.getRandomSyntax());
        gameModel.setNextPrompt(fileManager.getRandomSyntax());
        gameModel.setLastPrompt(fileManager.getRandomSyntax());
    }
    //Run by GameView. Upon selecting aa new category, fileManager sets new category.
    //changeLanguage already handles prompts, so no need to set prompts.
    public void configureNewLanguage(String s)
    {
        fileManager.changeLanguage(s);
    }

    //Run when player finishes typing current prompt.
    //Adds prompt's length to score, and shifts prompts down one.
    public void cycleNextPrompt()
    {
        addToCurrentScore(gameModel.getCurrentPrompt().length());
        gameModel.setCurrentPrompt(gameModel.getNextPrompt());
        gameModel.setNextPrompt(gameModel.getLastPrompt());
        gameModel.setLastPrompt(fileManager.getRandomSyntax());
    }

    //Configures prompt input to user input & returns hex color code.
    //If user input is the leftmost substring of prompt, text is black
    //If the user input is the same as input, text is black and cycleNextPrompt is run.
    //If the user is not the leftmost substring of prompt, text is red.
    public String compareInputToPrompt(String s)
    {
        int length = s.length();
        if(gameModel.getCurrentPrompt().length()<length || !s.equals(gameModel.getCurrentPrompt().substring(0,length)))
        {
            //should be red
            gameModel.setUserInput(s);
            return "ff4642";
        }
        else
        {
            //should be black
            if(length==gameModel.getCurrentPrompt().length())
            {
                gameModel.setShouldClear(true);
                cycleNextPrompt();
            }
            else
            {
                gameModel.setUserInput(s);
            }
            if(gameModel.getIsDarkTheme())
                return "e8e8e8";
            return "282828";
        }
    }

    //Get and Set for shouldClear.
    public boolean getShouldClear()
    {
        return gameModel.getShouldClear();
    }
    public void setShouldClear(boolean b)
    {
        gameModel.setShouldClear(b);
    }

    //Returns the proper css file depending on whether dark or light theme is requested.
    public String getCSSFile(boolean isDark)
    {
        if(isDark)
            return "cssThemes/darkTheme.css";
        else
            return "cssThemes/lightTheme.css";
    }

    //Returns current/high scores.
    public int getCurrentScore(){return gameModel.getCurrentScore();}
    public int getHighScore(){return gameModel.getHighScore();}

    //Adds integer to current score.
    public void addToCurrentScore(int n)
    {
        gameModel.setCurrentScore(gameModel.getCurrentScore()+n);
    }

    //Clears current score.
    public void clearCurrentScore()
    {
        gameModel.setCurrentScore(0);
    }

    //Changes variable that keeps track of whether it is dark or not.
    public void setIsDark(boolean isDark)
    {
        gameModel.setIsDarkTheme(isDark);
    }

    //Sets timer's displayed value. Converts int into time string.
    public void setTimerValue(int value)
    {
        gameModel.setTimerValue(value/60 + ":" +(value%60)/10+(value%60)%10);
    }

    //Clears current score and begins timer.
    public void beginTimer()
    {
        clearCurrentScore();
        gameTimer.scheduleAtFixedRate(gameTimerTask,0,1000);
    }

    //Run when gameView thinks a test should be started.
    //If the player is not already testing, testing is set to true and the timer begins.
    public void shouldStartTest()
    {
        if(!gameModel.getIsTesting())
        {
            gameModel.setIsTesting(true);
            beginTimer();
        }
    }

    //Timer thread is canceled.
    //Testing is set to false.
    //GameTimerTask is rebuild.
    //Timer value reset.
    public void endTest()
    {
        gameTimerTask.cancel();
        gameModel.setIsTesting(false);
        createGameTimerTask();
        gameModel.setTimerValue("1:00");
    }

    //Handles test completion.
    //Test is ended, and playerManagerKey is created.
    //This key is used to access the high-score of a player's test, which is within PlayerManager.
    //sets DisplayResults to true.
    public void completeTest()
    {
        endTest();
        setShouldClear(true);
        String playerManagerKey = fileManager.getCurrentLanguage()+fileManager.getCurrentCategory();
        playerManager.setHighScore(playerManagerKey,Math.max(playerManager.getHighScore(playerManagerKey), gameModel.getCurrentScore()));
        gameModel.setHighScore(playerManager.getHighScore(playerManagerKey));
        gameModel.setShouldDisplayResults(true);
    }

    //sets shouldDisplayResults to false.
    public void clearResultsBool()
    {
        gameModel.setShouldDisplayResults(false);
    }
    //Closes all timer-related threads.
    //Run by MainApplication upon exit.
    public void closeAllThreads()
    {
        gameTimerTask.cancel();
        gameTimer.cancel();
    }

}
