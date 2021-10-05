package hangman;

public class SimpleGuesser extends Guesser{
    // letters to guess
    private String myLetters;

    public SimpleGuesser(int numGuesses) { // could use inheritance
        super(numGuesses);
        myLetters = HangmanGame.LETTERS_ORDERED_BY_FREQUENCY;
    }

    @Override
    public String getMyLetters() {
        return myLetters;
    }

    @Override
    public void setMyLetters(String myLetters) {
        this.myLetters = myLetters;
    }
}
