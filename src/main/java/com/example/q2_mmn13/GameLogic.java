package com.example.q2_mmn13;
/*This class implements Hang man game logic
* The class reads a list of words from a file, and then picks a random word, and replaces each letter with "_" sign
* The class provides the number of wrong guesses and whether the game is over or not
* The class provides an option to restart without constructing a new instance of the class */
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameLogic {
    private String pickedWord;
    private String redactedWord;
    private ArrayList<String> wordsList;
    private int numberOfWrongGuess;

    private final int buf = 1000;
    final  int MAX_NUMBER_OF_GUESSES = 8;

    public GameLogic() {//constructor
        numberOfWrongGuess = 0;
        try {

            FileReader fr = new FileReader("myFile.txt"); //the text file contains list of words separated by ',' and ends with '.'
            char[] data = new char[buf];
            fr.read(data);
            String s = new String(data);
            wordsList = addWordsToArray(s);
            pickedWord = pickRandomWord().toLowerCase();
            redactWord();
            fr.close();

        }catch (IOException e){
            pickedWord = "-1";
        }


    }
    private ArrayList<Integer> getGuessIndexes(char guess){ // find the indexes of the guessed letter in the word
        ArrayList<Integer> tmp =new ArrayList<>();
        int i = pickedWord.indexOf(guess);
        while (i>=0){
            tmp.add(i);
            i = pickedWord.indexOf(guess,i+1);
        }

        return tmp;
    }


    public boolean isGoodGuess(char guess){ // Check if the guessed letter is in the word, and increase number of wrong guesses if needed
        if (pickedWord.indexOf(guess) != -1){
            return true;
        }else {
            numberOfWrongGuess++;
            return false;
        }
    }

    public void restart(){ // restarts the game - pick new word and redact it
        wordsList.remove(pickedWord);
        pickedWord = pickRandomWord();
        pickedWord = pickedWord.toLowerCase();
        redactWord();
        numberOfWrongGuess = 0;

    }
    public String computeNext(char guess){// recovers the letter in the redacted word

        ArrayList<Integer> tmp =  getGuessIndexes(guess);
        for (Integer index : tmp) {
            redactedWord = redactedWord.substring(0, index) + guess + redactedWord.substring(index + 1);
        }
        return redactedWord;

    }

    public boolean isGameOver(){//checks if the number of guesses equal to the maximum allowed
        return numberOfWrongGuess >= MAX_NUMBER_OF_GUESSES;
    }

    public boolean isGameWon(){ // checks if the redacted word is equal to the original word

        return Objects.equals(redactedWord, pickedWord);
    }

    private String pickRandomWord() { // picks random word out of an array
        Random random = new Random();
        int randomInt = random.nextInt(wordsList.size());
        return wordsList.get(randomInt);
    }

    private ArrayList<String> addWordsToArray(String s){//adds words from the file to array
        int EOF = s.indexOf('.');
        s = s.substring(0,EOF);
        String[] arrSplit = s.split(", ");
        return new ArrayList<>(Arrays.asList(arrSplit));
    }

    private void redactWord(){//redact the picked word
        StringBuilder s  = new StringBuilder();
        for (int i = 0; i<pickedWord.length(); i++){
            if (pickedWord.charAt(i) != ' '){
                s.append('_');
            }else{
                s.append(' ');
            }
            redactedWord = String.valueOf(s);
        }
    }



    public String getRedactedWord() {
        return redactedWord;
    }

    public int getNumberOfWrongGuess() {
        return numberOfWrongGuess;
    }

    public String getPickedWord() {
        return pickedWord;
    }
    public boolean isMoreWords(){
        return wordsList.get(0) != null;
    }

}
