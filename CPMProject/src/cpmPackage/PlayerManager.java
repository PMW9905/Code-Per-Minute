package cpmPackage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private HashMap<String,Integer> highestCPM; // Stores the highest CPM for a particular categorical key (Key is
    //The key is equal to the string concat of currentLanguage and currentCategory.
    public PlayerManager()
    {
        highestCPM = new HashMap<>();
        readPlayerData();
    }
    //Reads in player data from files.
    public void readPlayerData()
    {
        BufferedReader br;
        String buffer;
        int index;
        try
        {
            br = new BufferedReader(new FileReader("src/playerData.txt"));
            while((buffer=br.readLine())!=null)
            {
                index = buffer.indexOf("~");
                if(index<0)
                {
                    throw new IOException();
                }
                highestCPM.put(buffer.substring(0,index),Integer.parseInt(buffer.substring(index+1)));
            }
            br.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to find file for player data while trying to read.");
            e.printStackTrace();
        }
        catch(Exception e) {
            System.out.println("Exception thrown whilst loading player data.");
            e.printStackTrace();
        }

    }

    //Run only upon exiting
    //Stores contents of map into playerData.txt
    public void writePlayerData()
    {
        BufferedWriter bw;
        try
        {
            bw = new BufferedWriter(new FileWriter("src/playerData.txt"));
            for(Map.Entry<String,Integer> entry : highestCPM.entrySet())
                bw.write(entry.getKey()+"~"+entry.getValue()+"\n");
            bw.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to find playerData while attempting to exit.");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.out.println("Exception thrown while writing player data.");
            e.printStackTrace();
        }
    }

    //Returns high score.
    //Returns -1 if no prior high score.
    public int getHighScore(String category)
    {
        if(highestCPM.containsKey(category))
            return highestCPM.get(category);
        return -1;
    }
    //Sets high score
    public void setHighScore(String category, int high)
    {
        highestCPM.put(category,high);
    }
}
