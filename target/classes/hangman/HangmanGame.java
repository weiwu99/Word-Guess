package hangman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.DisplayWord;
import util.HangmanDictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HangmanGame {
    // constants
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String LETTERS_ORDERED_BY_FREQUENCY = "etaoinshrldcumfpgwybvkxjqz"; //shouldn't be private I believe


    // Image set for hangman:
    // https://www.tutorialspoint.com/how-to-display-an-image-in-javafx
    // Images: https://www.oligalma.com/en/downloads/images/hangman
    // intentionally change the address due to privacy concerns
    private  Image image0 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\0.jpg"));
    private  Image image1 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\1.jpg"));
    private  Image image2 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\2.jpg"));
    private  Image image3 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\3.jpg"));
    private  Image image4 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\4.jpg"));
    private  Image image5 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\5.jpg"));
    private  Image image6 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\6.jpg"));
    private  Image image7 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\7.jpg"));
    private  Image image8 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\8.jpg"));
    private  Image image9 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\9.jpg"));
    private  Image image10 = new Image(new FileInputStream("C:\\Users\\y\\OneDrive\\Documents\\College\\Senior\\Fall\\CS 307\\hangman_ww148\\data\\10.jpg"));

    private List<Image> hangmanImages;

    // current player's guess
    private String myGuess;
    // current guess
    private int myIndex;

    //player role:
    private Guesser guesser;
    private Keeper keeper;

    //Game Mode:
    private String gameType;

    // JFX variables
    private Scene myScene;
    private Timeline myAnimation;
    private Text myNumGuessesLeftDisplay;
    private List<Text> mySecretWordDisplay;
    private List<Text> myLettersLeftToGuessDisplay;
    private Image myHangmanDisplay;
    private ImageView hangmanBackground;

    /**
     * Create Hangman game with the given dictionary of words to play a game with words
     * of the given length and giving the user the given number of chances.
     */
    public HangmanGame (HangmanDictionary dictionary, int wordLength, int numGuesses, String gameMode) throws FileNotFoundException {
        gameType = gameMode;
        hangmanImages = new ArrayList<Image>(
                Arrays.asList(image0, image1, image2, image3, image4, image5, image6, image7, image8, image9, image10)
                );

        if (numGuesses <=0 ) {
            numGuesses = 10;
            System.out.print("Your number of guesses is not correct. It has been set to default value: 10");
        }

        if (gameMode.equals("Simple")) {
            guesser = new SimpleGuesser(numGuesses);
            keeper = new SimpleKeeper(dictionary, wordLength);
            myIndex = 0;
        }
        else if (gameMode.equals("Inter")) {
            guesser = new InteractiveGuesser(numGuesses);
            keeper = new InteractiveKeeper(wordLength);
        }
        else if (gameMode.equals("Clever")) {
            guesser = new CleverGuesser(dictionary, wordLength, numGuesses);
//            guesser = new SimpleGuesser(numGuesses);
            keeper = new CleverKeeper(dictionary, wordLength);
//            keeper = new SimpleKeeper(dictionary, wordLength);
            myIndex = 0;
        }

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
        showLetters(root, ALPHABET, 50, 50, 20, myLettersLeftToGuessDisplay);

//        // show "hanged man" simply as a number that counts down
        myNumGuessesLeftDisplay = new Text(width - 150, height/2, "Lives left: "+guesser.getMyNumGuessesLeft());
        myNumGuessesLeftDisplay.setFont(Font.font("Verdana", FontWeight.LIGHT, 15));
        root.getChildren().add(myNumGuessesLeftDisplay);

        //Setting image to the image view
        myHangmanDisplay = hangmanImages.get(10-guesser.getMyNumGuessesLeft());
        //Creating the image view
        hangmanBackground = new ImageView(myHangmanDisplay);
//        Setting the image view parameters
        hangmanBackground.setX(width/2 - 100 );
        hangmanBackground.setY(height/2 - 200);
        hangmanBackground.setFitWidth(400);
        hangmanBackground.setFitHeight(400);
        hangmanBackground.setPreserveRatio(true);
        root.getChildren().add(hangmanBackground);


        // show word being guessed, with letters hidden until they are guessed
        mySecretWordDisplay = new ArrayList<>();
        showLetters(root, keeper.getMyDisplayWord().toString(), 200, 500, 40, mySecretWordDisplay);
        // create place to see and interact with the shapes
        myScene = new Scene(root, width, height, background);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return myScene;
    }

    private void showLetters(Group root, String wordDisplay, int x, int y, int size, List listDisplay) {
        for (int k = 0; k < wordDisplay.length(); k += 1) {
            Text displayLetter = new Text(x+20*k, y, wordDisplay.substring(k, k+1));
            displayLetter.setFont(Font.font("Arial", FontWeight.BOLD, size));
            listDisplay.add(displayLetter);
            root.getChildren().add(displayLetter);
        }
    }

    /**
     * Play one round of the game.
     */
    public void playGuess () {
        // has user guessed?
        if (myGuess == null) { // game
            return;
        }
        // handle only valid guesses
        handleGuesses();

        myGuess = null;

        // check for end of game - game
        endOfGame();
    }

    /**
     * Game processes guesses
     */
    private void handleGuesses() {
        if (myGuess.length() == 1 && ALPHABET.contains(myGuess)) {
            // record guess character - guesser
            guesser.recordGuess(myGuess);
            // check for guess in secret word - keeper
            keeper.checkGuess(myGuess, guesser);
            // update letters displayed to the user - game
            updateLetters();
        }
        else {
            System.out.println("Please enter a single alphabetic letter ...");
        }
    }

    /**
     * Game updates letter display to the guesser
     */
    private void updateLetters() {
        for (int k = 0; k < guesser.getMyLettersLeftToGuess().length(); k += 1) {
            myLettersLeftToGuessDisplay.get(k).setText(guesser.getMyLettersLeftToGuess().substring(k, k+1));
        }
        myNumGuessesLeftDisplay.setText("Lives left: "+guesser.getMyNumGuessesLeft());
        for (int k = 0; k < keeper.getMyDisplayWord().toString().length(); k += 1) {
            mySecretWordDisplay.get(k).setText(keeper.getMyDisplayWord().toString().substring(k, k+1));
        }

        // Change images:
        // https://stackoverflow.com/questions/29500761/javafx-change-the-image-in-an-imageview
        myHangmanDisplay = hangmanImages.get(10-guesser.getMyNumGuessesLeft());
        hangmanBackground.setImage(myHangmanDisplay);
    }

    /**
     * Check if the game ends
     */
    private void endOfGame() {
        if (guesser.getMyNumGuessesLeft() == 0) {
            endScene("YOU ARE HUNG!!!");
        }
        else if (keeper.getMyDisplayWord().equals(keeper.getMySecretWord())) {
            endScene("YOU WIN!!!");
        }
    }

    private void endScene(String s) {
        System.out.println(s);
        // stop responding to key events when game is over
        myScene.setOnKeyPressed(null);
        // stop guessing when game is over
        myAnimation.stop();
    }


    // Record user's input to be used in the game loop
    private void makeGuess () {
        if (gameType.equals("Inter")) {
            playGuess();
        }
        else if (gameType.equals("Simple")) {
            myGuess = "" + guesser.getMyLetters().charAt(myIndex++); // myIndex should be part of guesser, but it's easier to be an instance in game
            playGuess();
        }
        else if (gameType.equals("Clever")) {
            myGuess = guesser.cleverGuess(keeper); // myIndex should be part of guesser, but it's easier to be an instance in game
            playGuess();
        }
    }

    // Record user's input to be used in the game loop
    private void handleKeyInput (KeyCode code) {
        myGuess = code.getChar().toLowerCase();
    }

}
