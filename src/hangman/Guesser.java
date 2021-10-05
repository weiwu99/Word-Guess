package hangman;

public class Guesser {

    // tracks letters guessed
    private StringBuilder myLettersLeftToGuess;
    // tracks past guesses
    private StringBuilder myPastGuesses;
    // how many guesses are remaining
    private int myNumGuessesLeft;

    public Guesser(int numGuesses) { // could use inheritance but chose not to
        myNumGuessesLeft = numGuesses;
        myLettersLeftToGuess = new StringBuilder(HangmanGame.ALPHABET);
        myPastGuesses = new StringBuilder("");
    }

    /**
     * Guesser records guesses
     * @return if guesses are unique
     */
    public void recordGuess(String myGuess) {
        int index = myLettersLeftToGuess.indexOf(myGuess);
        // do not count repeated guess as a miss
        if (index < 0) {
            return;
        }
        // record guess
        myLettersLeftToGuess.setCharAt(index, ' ');
        myPastGuesses.append(myGuess);
    }

    /**
     * get myGuess for clever Guesser
     * @return
     */
    public String cleverGuess(Keeper keeper) { return "";}

    /*
    A bunch of getters and setters
     */
    public void setMyLettersLeftToGuess(StringBuilder myLettersLeftToGuess) {
        this.myLettersLeftToGuess = myLettersLeftToGuess;
    }

    public void setMyNumGuessesLeft(int myNumGuessesLeft) {
        this.myNumGuessesLeft = myNumGuessesLeft;
    }

    public StringBuilder getMyLettersLeftToGuess() {
        return myLettersLeftToGuess;
    }

    public int getMyNumGuessesLeft() {
        return myNumGuessesLeft;
    }

    public String getMyLetters() {return "hello";}

    public void setMyLetters(String myLetters) {return;}

    public StringBuilder getMyPastGuesses() {return myPastGuesses;    }

    public void setMyPastGuesses(StringBuilder myPastGuesses) {this.myPastGuesses = myPastGuesses;}

}
