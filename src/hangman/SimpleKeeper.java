package hangman;

import util.DisplayWord;
import util.HangmanDictionary;

public class SimpleKeeper extends Keeper{

    // word that is being guessed
    private String mySecretWord;
    // what is shown to the user
    private DisplayWord myDisplayWord;

    public SimpleKeeper(HangmanDictionary dictionary, int wordLength) {
        super();
        mySecretWord = dictionary.getRandomWord(wordLength).toLowerCase();
        myDisplayWord = new DisplayWord(mySecretWord);
        // SHOULD NOT PUBLIC, but makes it easier to test
//        System.out.println("*** " + mySecretWord);
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
