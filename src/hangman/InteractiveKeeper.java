package hangman;

import javafx.scene.control.TextInputDialog;
import util.DisplayWord;
import util.HangmanDictionary;

import java.util.Locale;
import java.util.Optional;

public class InteractiveKeeper extends Keeper{


    // word that is being guessed
    private String mySecretWord;
    // what is shown to the user
    private DisplayWord myDisplayWord;


    public InteractiveKeeper(int wordLength) {
        super();
        mySecretWord = getInput(String.format("Please enter a secret word %d letters long", wordLength), wordLength).toLowerCase();
        myDisplayWord = new DisplayWord(mySecretWord);

        // SHOULD NOT PUBLIC, but makes it easier to test
        System.out.println("*** " + mySecretWord);
    }

    @Override
    public void checkGuess(String myGuess, Guesser guesser) {
        if (! mySecretWord.contains(myGuess)) {
//            myNumGuessesLeft -= 1;
            int myNumGuessesLeft = guesser.getMyNumGuessesLeft();
            myNumGuessesLeft -= 1;
            guesser.setMyNumGuessesLeft(myNumGuessesLeft);
        }
        else {
            myDisplayWord.update(myGuess.charAt(0), mySecretWord);
        }
    }

    // Get user's text input to use as the secret word
    private String getInput (String prompt, int numCharacters) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(prompt);
        // DO NOT USE IN GENERAL - this is a TERRIBLE solution from UX design, and we will better ways later
        Optional<String> answer = dialog.showAndWait();
        while (answer.isEmpty() || answer.get().length() != numCharacters) {
            answer = dialog.showAndWait();
        }
        return answer.get();
    }


    /*
    A bunch of getters and setters
     */
    @Override
    public void setMySecretWord(String mySecretWord) {
        this.mySecretWord = mySecretWord;
    }

    @Override
    public DisplayWord getMyDisplayWord() {
        return myDisplayWord;
    }

    @Override
    public void setMyDisplayWord(DisplayWord myDisplayWord) {
        this.myDisplayWord = myDisplayWord;
    }

    @Override
    public String getMySecretWord() {
        return mySecretWord;
    }
}
