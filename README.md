# Code Per Minute - Typing Trainer for Programmers

Code Per Minute is a JavaFX & CSS GUI application with the intent of providing users with an effective way of improving typing speeds in specific programming languages.

A critical aspect of learning to type faster is pattern recognition. While modern typing tests do a great job of covering common words and phrases, programming syntax is often very removed from traditional english/language. As such, gains in wpm on sites such as 10fastfingers.com and nitrotype.com may not carry over as well into c++ or Java.

Code Per Minute exist to solve this problem. By orgaizing typing tests by language and category, CPM allows the user to easily practice subsets of a particular language, thus building muscle memory and pattern recognition for programming-specific syntax. A user may even create their own language files/categories to further expand their learning and practice.

## Adding Languages

To add custom languages, first create a txt file that will contain the syntax you wish to add.
To create a category within the language file, first write a "\~" followed by the name of the category.
All keywords/syntax that you wish to be contained in this category should be placed on new lines, with no "\~" prefix.
This process can be repeated for any number of categories and keywords/syntax you desire.

Example:

\~Data Creation / Manipulation 
int num = 0;

char character = '\0';

double decimal;

\~Java Keywords

abstract

assert

boolean

break

Once you have created your language file, navigate to the languagefilePathways.txt file.
Create a new line and add your language in the format of Language_Name:Language_File_Pathway

Example: 

Java:src/javaKeywords.txt

After that, you're all good to go! Simply launch CPM. It will read languageFilePathways and configure your language automatically.
