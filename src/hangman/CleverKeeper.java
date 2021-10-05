package hangman;

import util.DisplayWord;
import util.HangmanDictionary;

import java.util.*;
import java.util.stream.IntStream;

public class CleverKeeper extends Keeper{
    // word that is being guessed
    private String mySecretWord;
    // what is shown to the user
    private DisplayWord myDisplayWord;
    private List<String> wordList;
    private HashMap<String, List<String>> dictPattern;

    // create only one and reuse it, so you get a truly random sequence of values
    private Random myRandom;

    public CleverKeeper(HangmanDictionary dictionary, int wordLength) {
        super();
        mySecretWord = dictionary.getRandomWord(wordLength).toLowerCase();
        myDisplayWord = new DisplayWord(mySecretWord);
        // SHOULD NOT PUBLIC, but makes it easier to test
//        System.out.println("*** " + mySecretWord);
//        System.out.println("****** " + myDisplayWord.toString().replaceAll("\\s", ""));

        wordList = dictionary.getWords(wordLength);
        myRandom = new Random();
        dictPattern = new HashMap<String, List<String>>();
    }

    @Override
    public void checkGuess(String myGuess, Guesser guesser) {

        // OG stuff
        if (! mySecretWord.contains(myGuess)) {
            int myNumGuessesLeft = guesser.getMyNumGuessesLeft();
            myNumGuessesLeft -= 1;
            guesser.setMyNumGuessesLeft(myNumGuessesLeft);
        }
        else {
            myDisplayWord.update(myGuess.charAt(0), mySecretWord);
        }

        // TODO: change secret to a new word
        String wordDisplayPattern = myDisplayWord.toString().replaceAll("\\s", ""); // wordDisplayPattern: key to record the pattern

        //TODO: test
//        System.out.println("Word pattern is: " + wordDisplayPattern);

        // loop thru each char and find remaining words with the same pattern
        updateSecretWords(myGuess, wordDisplayPattern, guesser);
//
//        for (String i: dictPattern.keySet() ) {
//            System.out.println("Key after: " + i);
//            System.out.println("Value: " + dictPattern.get(i));
//        }

        // SHOULD NOT PUBLIC, but makes it easier to test
//        System.out.println("*** " + mySecretWord);
//        System.out.println("****** " + myDisplayWord.toString().replaceAll("\\s", ""));
//        System.out.println("############################# END ROUND #############################");
    }

    private void updateSecretWords(String myGuess, String wordDisplayPattern, Guesser guesser) {

        // not sure what to do w/ duplicates
        for (int i = 0; i < wordList.size(); i++) {
            int numSamePattern = 0;
            int numBlank = 0;

            String currentWord = wordList.get(i);
            for (int j = 0; j < wordList.get(i).length(); j++) {
                String localLetter = wordDisplayPattern.substring(j, j+1);
                String wordLetter = currentWord.substring(j,j+1);

                numBlank = countNumCondition(numBlank, !Character.isAlphabetic(localLetter.charAt(0))); // count "_" in wordDisplayPattern

                numSamePattern = countNumAlphabet(numSamePattern, localLetter, wordLetter); // count letters
            }

            // all matched in terms of pattern
            if (numSamePattern == currentWord.length() - numBlank) {
                dictPattern.putIfAbsent(wordDisplayPattern, new ArrayList<>()); //t -> []

                if(dictPattern.get(wordDisplayPattern) != null) {
                    // TODO: if the input word has past guesses, don't put it in the dict
                    // if contains past guesses, won't match
                    getUniqueElements(wordDisplayPattern, currentWord, guesser);
                }
            }
        }

//        System.out.println(guesser.getMyPastGuesses().toString());
//        System.out.println("The current pattern is: " + wordDisplayPattern);
//        System.out.println("Value: " + dictPattern.get(wordDisplayPattern));
        // Change the secret word from modified wordList;
//        removeExtraWords(myGuess, wordDisplayPattern);

        this.mySecretWord = getNewRandomWord(dictPattern.get(wordDisplayPattern));
    }


    private int countNumAlphabet(int numSamePattern, String localLetter, String wordLetter) {
        if (Character.isAlphabetic(localLetter.charAt(0))){
            // update wordList so that only words with the same pattern will stay
            numSamePattern = countNumCondition(numSamePattern, wordLetter.equals(localLetter));
        }
        return numSamePattern;
    }

    private int countNumCondition(int num, boolean condition) {
        if (condition) {
            num++;
        }
        return num;
    }

    private boolean containPastGuesses(String word, Guesser guesser, String wordDisplayPattern) {
        String pastGuesses = guesser.getMyPastGuesses().toString();

//        // TODO: Figure out how the hell I can get it work
        for (int j = 0; j < word.length(); j++) { // no current letter
            if (pastGuesses.indexOf(word.substring(j, j+1)) != -1 ) { //contain past guessed chars
                if (wordDisplayPattern.indexOf(word.substring(j, j+1)) == -1) { // except for those containing the chars within the pattern - good ones
                    return true;
                }
            }
        }
        return false;
    }

    // https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
    private int countOccurrencesChar (String word, String localLetter) {
        int count = 0;

        String wordCopy = word.replace(localLetter, "");
        count = word.length() - wordCopy.length();

        return count;
    }

    private boolean kickWord(String wordDisplayPattern, String word) {

        for (int i = 0; i < wordDisplayPattern.length(); i++) {
            String localLetter = wordDisplayPattern.substring(i, i+1);

            if (Character.isAlphabetic(localLetter.charAt(0))){

                int patternCountLetter = countOccurrencesChar(wordDisplayPattern, localLetter);
                int wordCountLetter = countOccurrencesChar(word, localLetter);

                if (patternCountLetter != wordCountLetter) {
                    return true;
                }
            }
        }
        return false;
    }

    // Remove words w/ multiple existing chars on the pattern
    // or the ones that contains the guessed char
    private void removeExtraWords(String myGuess, String wordDisplayPattern) {
        List<String> viableWords = dictPattern.get(wordDisplayPattern);
        for (int i = 0; i < viableWords.size(); i++) {
            if ((kickWord(wordDisplayPattern, viableWords.get(i))) || (viableWords.get(i).indexOf(myGuess) != -1)) {
                viableWords.remove(viableWords.get(i));
                i--;
            }
        }
    }

    private void getUniqueElements(String wordDisplayPattern, String word, Guesser guesser) {
        // unique words, no duplicates
        if (!dictPattern.get(wordDisplayPattern).contains(word)) {
            if (!containPastGuesses(word, guesser, wordDisplayPattern)) { // if the word contains past guessed chars
                dictPattern.get(wordDisplayPattern).add(word);
            }
        }
    }

    private String getNewRandomWord(List<String> list) {
        // are there any words of this length left?
        return (list.size() > 0) ? list.get(myRandom.nextInt(list.size())) : "";
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
