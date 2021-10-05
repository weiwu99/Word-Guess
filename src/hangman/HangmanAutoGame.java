package hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.DisplayWord;
import util.HangmanDictionary;


/**
 * This class represents a game of Hangman between:
 *  - a guesser that chooses letters automatically based on their commonality
 *  - an interactive secret word keeper that lets the user choose the secret word
 *
 * @author Robert C. Duvall
 */
public class HangmanAutoGame {
    // constants
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERS_ORDERED_BY_FREQUENCY = "etaoinshrldcumfpgwybvkxjqz";

    // word that is being guessed
    private String mySecretWord;
    // how many guesses are remaining
    private int myNumGuessesLeft;
    // what is shown to the user
    private DisplayWord myDisplayWord;
    // tracks letters guessed
    private StringBuilder myLettersLeftToGuess;
    // current player's guess
    private String myGuess;
    // letters to guess
    private String myLetters;
    // current guess
    private int myIndex;
    // JFX variables
    private Scene myScene;
    private Timeline myAnimation;
    private Text myNumGuessesLeftDisplay;
    private List<Text> mySecretWordDisplay;
    private List<Text> myLettersLeftToGuessDisplay;


    /**
     * Create Hangman game with the given dictionary of words to play a game with words
     * of the given length and giving the user the given number of chances.
     */
    public HangmanAutoGame (HangmanDictionary dictionary, int wordLength, int numGuesses) {
        myNumGuessesLeft = numGuesses;
        myLettersLeftToGuess = new StringBuilder(ALPHABET);
        mySecretWord = getInput(String.format("Please enter a secret word %d letters long", wordLength), wordLength);
        myDisplayWord = new DisplayWord(mySecretWord);
        myLetters = LETTERS_ORDERED_BY_FREQUENCY;
        myIndex = 0;
        // SHOULD NOT PUBLIC, but makes it easier to test
        System.out.println("*** " + mySecretWord);
    }

    /**
     * Start the game by animating the display of changes in the GUI every speed seconds.
     */
    public void start (double speed) {
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(speed), e -> makeGuess()));
        myAnimation.play();
    }

    /**
     * Create the game's "scene": what shapes will be in the game and their starting properties.
     */
    public Scene setupDisplay (int width, int height, Paint background) {
        Group root = new Group();
        // show letters available for guessing
        myLettersLeftToGuessDisplay = new ArrayList<>();
        for (int k = 0; k < ALPHABET.length(); k += 1) {
            Text displayLetter = new Text(50+20*k, 50, ALPHABET.substring(k, k+1));
            displayLetter.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            myLettersLeftToGuessDisplay.add(displayLetter);
            root.getChildren().add(displayLetter);
        }
        // show "hanged man" simply as a number that counts down
        myNumGuessesLeftDisplay = new Text(width/2 - 100, height/2 + 100, ""+myNumGuessesLeft);
        myNumGuessesLeftDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 300));
        root.getChildren().add(myNumGuessesLeftDisplay);
        // show word being guessed, with letters hidden until they are guessed
        mySecretWordDisplay = new ArrayList<>();
        for (int k = 0; k < myDisplayWord.toString().length(); k += 1) {
            Text displayLetter = new Text(200+20*k, 500, myDisplayWord.toString().substring(k, k+1));
            displayLetter.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            mySecretWordDisplay.add(displayLetter);
            root.getChildren().add(displayLetter);
        }
        // create place to see the shapes
        myScene = new Scene(root, width, height, background);
        return myScene;
    }

    /**
     * Play one round of the game.
     */
    public void playGuess () {
        // has user guessed?
        if (myGuess == null) {
            return;
        }
        // handle only valid guesses
        if (myGuess.length() == 1 && ALPHABET.contains(myGuess)) {
            int index = myLettersLeftToGuess.indexOf(myGuess);
            // do not count repeated guess as a miss
            if (index < 0) {
                return;
            }
            // record guess
            myLettersLeftToGuess.setCharAt(index, ' ');
            // check for guess in secret word
            if (! mySecretWord.contains(myGuess)) {
                myNumGuessesLeft -= 1;
            }
            else {
                myDisplayWord.update(myGuess.charAt(0), mySecretWord);
            }
            // update letters displayed to the user
            for (int k = 0; k < myLettersLeftToGuess.length(); k += 1) {
                myLettersLeftToGuessDisplay.get(k).setText(myLettersLeftToGuess.substring(k, k+1));
            }
            myNumGuessesLeftDisplay.setText(""+myNumGuessesLeft);
            for (int k = 0; k < myDisplayWord.toString().length(); k += 1) {
                mySecretWordDisplay.get(k).setText(myDisplayWord.toString().substring(k, k+1));
            }
        }
        else {
            System.out.println("Please enter a single alphabetic letter ...");
        }
        myGuess = null;

        // check for end of game
        if (myNumGuessesLeft == 0) {
            System.out.println("YOU ARE HUNG!!!");
            // stop guessing when game is over
            myAnimation.stop();
        }
        else if (myDisplayWord.equals(mySecretWord)) {
            System.out.println("YOU WIN!!!");
            // stop guessing when game is over
            myAnimation.stop();
        }
    }

    // Record user's input to be used in the game loop
    private void makeGuess () {
        myGuess = "" + myLetters.charAt(myIndex++);
        playGuess();
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
}
