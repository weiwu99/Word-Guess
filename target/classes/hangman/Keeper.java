package hangman;

import javafx.scene.control.TextInputDialog;
import util.DisplayWord;
import util.HangmanDictionary;

import java.util.Optional;

public class Keeper {

    // word that is being guessed
    private String mySecretWord;
    // what is shown to the user
    private DisplayWord myDisplayWord;

    public Keeper() {
    }

    public void checkGuess(String myGuess, Guesser guesser) {
    }

    /*
    A bunch of getters and setters
     */
    public void setMySecretWord(String mySecretWord) {}

    public DisplayWord getMyDisplayWord() {return myDisplayWord;}

    public void setMyDisplayWord(DisplayWord myDisplayWord) {}

    public String getMySecretWord() {return mySecretWord;}

}
