# DAWG: Directed Acyclic Word Graph 

## This project uses a DAWG to store and search a large dictionary.
# Authors
* Emmett Jaakkola, Kyler Nikolai, Thomas Anderson
  
## Features

* Builds a minimal DAWG from a CSV file
* WebAssembly client to take user input, indicates any incorrectly spelt words, 
gives suggestions for the closest words based on Levenshtein distance
* Range gives the distance away from the input word
* First Letter returns a list of all similar words starting with the same first letter

## File Explanation
* DAWG.java builds the Directly Acyclic Worded Graph
* FindWords.java uses the Levenshtein distance between the input word and the words stored in the dawg
to find the closest correctly spelled word
* Load.java converts the cleaned CSV file into a string to be passed to DAWG.java
* Print.java is used for debugging purposes
* App.java runs the program with an input word, gives the amount of states found, how many similar words have been found, and how long it took to run
* CSVSorter.java takes a CSV file input, removes any repeated words, any words not within the language [a-zA-Z] and then outputs a cleaned file

## How To Use

* Cleaning a file: To clean a file you need to import a single column dictionary using a CSV file into the Assests file. In the CSVSorter.java file's main function, type in the name of the file imported and type in a name to call the cleaned file. Run.

* Running DAWG: To build the DAWG, open Load.java and change the string for the InputFile (line 12) to be the same as your newly cleaned CSV file. Then from App.java run the file. (On line 27 there is an input word to demonstraite in terminal)

  ### Use on GitHub: <a href="https://thomasandersonhance.github.io/MSCS-271-Final-Project/website/">Link to Website</a>

  * Type in misspelled word, right click on incorrect word, pick correct word from list
  * Toggle first letter to get words only starting with the same first word
  * Change range to get more similar words to the misspelled word

<img width="695" alt="image" src="https://github.com/user-attachments/assets/1b31afcd-a5c9-429c-a0d1-e98caa5ecf4b" />
