import hangman.HangmanGame;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import hangman.HangmanAutoGame;
import hangman.HangmanInteractiveGame;
import util.HangmanDictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * This class launches the Hangman game and plays once.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    // constants - JFX
    public static final String TITLE = "HangPerson";
    public static final int SIZE = 600;
    public static final Paint BACKGROUND = Color.THISTLE;
    public static final double SECOND_DELAY = 1;
    // constants - Game
    public static final String DICTIONARY = "lowerwords.txt";

    // Testing cases from https://courses.cs.duke.edu/compsci101/spring14/assign/05_hangman/howto.php#Clever
//    public static final String DICTIONARY = "test.txt";

//    public static final int NUM_LETTERS = 6;
    public static final int NUM_LETTERS = 4; //testing

//    public static final int NUM_MISSES = 8; //26 - NUM_LETTERS - 1
    public static final int NUM_MISSES = 10; //26 - NUM_LETTERS - 1, fixed for now

    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) throws FileNotFoundException {

        // try my own
        HangmanGame game = new HangmanGame(new HangmanDictionary(DICTIONARY), NUM_LETTERS, NUM_MISSES, "Clever");

//        HangmanInteractiveGame game = new HangmanInteractiveGame(
//        HangmanAutoGame game = new HangmanAutoGame(
//                new HangmanDictionary(DICTIONARY), NUM_LETTERS, NUM_MISSES);
        stage.setScene(game.setupDisplay(SIZE, SIZE, BACKGROUND));
        stage.setTitle(TITLE);
        stage.show();

        game.start(SECOND_DELAY);
    }
}
