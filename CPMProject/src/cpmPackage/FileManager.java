package cpmPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private final Map<String,String> fileMap; //Stores a mapping of language names to their respective file paths.
    private Map<String, ArrayList<String>> syntaxMap; //Stores a mapping of Category to ArrayList reference.
    private ArrayList<String> allCategories; //Stores all current categories under a specific language.
    private ArrayList<String> currentSyntax; //Stores all current syntax prompts under the current category.
    private String currentLanguage;
    private String currentCategory;

    public FileManager()
    {
        fileMap = new HashMap<>();
        syntaxMap = new HashMap<>();
        allCategories = new ArrayList<>();
        currentSyntax = new ArrayList<>();
        initializeFileManager();
    }
    //Establishes the current category, current language,
    // reads in the language file pathways, and sets reads in the file data
    // of the current language.
    public void initializeFileManager()
    {
        currentCategory = "";
        currentLanguage=readLanguageFilePathways();
        readLanguageFile(currentLanguage);
        setCurrentCategory(allCategories.get(0));
    }

    //Reads in languageFilePathways.txt
    //Sends mappings of language name to path to fileMap
    //Returns the language that the language combo-box will default to.
    public String readLanguageFilePathways()
    {
        BufferedReader br;
        String fileLine;
        String defaultLanguageName = "";
        try
        {
            br = new BufferedReader(new FileReader("src/languageFilePathways.txt"));
            if((fileLine = br.readLine())==null)
                throw new Exception("Language Pathway File is empty.");
            defaultLanguageName = fileLine.substring(0,fileLine.indexOf(":"));
            do
            {
                int index = fileLine.indexOf(":");
                if(index<0)
                    throw new Exception("Incorrect formatting. ':' symbol not found while reading line.");
                else
                    fileMap.put(fileLine.substring(0,index),fileLine.substring(index+1));

            } while((fileLine = br.readLine())!=null);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to find file for languageFilePathway ");
            e.printStackTrace();
            System.out.println("\nExiting...");
            System.exit(-1);
        }
        catch (Exception e)
        {
            System.out.print("Exception caught while loading src/languageFilePathways.txt\n");
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        return defaultLanguageName;
    }

    //Reads in corresponding language file from language key string.
    //Stores data into syntaxMap and allCategories arraylist.
    public void readLanguageFile(String language)
    {
        String currentKey; //Stores the current category that serves as the key for syntaxMap
        String fileLocation = fileMap.get(language);
        String fileLine;
        ArrayList<String> current = new ArrayList<>(); //Stores syntax for currentKey until it can be placed into the syntaxMap
        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader(fileLocation));
            while((fileLine = br.readLine()) != null)
            {
                if(fileLine.charAt(0)=='~')
                {
                    currentKey=fileLine.substring(1);
                    allCategories.add(currentKey);
                    syntaxMap.put(currentKey, new ArrayList<>());
                    current = syntaxMap.get(currentKey);
                }
                else
                {
                    current.add(fileLine);
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to find file for language " + language);
            e.printStackTrace();
            System.out.println("\nExiting...");
            System.exit(-1);
        }
        catch (Exception e)
        {
            System.out.print("Exception caught while accessing "+language+" typing test files.");
            e.printStackTrace();
            System.out.println("\nExiting...");
            System.exit(-1);
        }
    }

    //Loads in data for specified language.
    public void changeLanguage(String language)
    {
        currentLanguage = language;
        syntaxMap = new HashMap<>();
        allCategories = new ArrayList<>();
        currentCategory = "";
        readLanguageFile(language);
        setCurrentCategory(allCategories.get(0));
    }

    //Retrieves current language/category.
    public String getCurrentLanguage(){return currentLanguage;}
    public String getCurrentCategory(){return currentCategory;}

    //Sets current category & updates currentSyntax arrayList.
    public void setCurrentCategory(String s)
    {
        currentCategory = s;
        currentSyntax = syntaxMap.get(currentCategory);
    }

    //Returns all Categories/Languages
    public ArrayList<String> getAllCategories(){return allCategories;}
    public ArrayList<String> getAllLanguages(){return new ArrayList<>(fileMap.keySet());}

    //Returns random syntax element from currentSyntax.
    public String getRandomSyntax(){return currentSyntax.get( (int)(Math.random()*(currentSyntax.size()) ) );}

}
