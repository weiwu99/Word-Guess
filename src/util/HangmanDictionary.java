package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * This class represents all possible words that might be used in a game.
 *
 * @author Michael Hewner
 * @author Owen Astrachan
 * @author Mac Mason
 * @author Robert C. Duvall
 */
public class HangmanDictionary {
    // map of length to all words of that length
    private Map<Integer, List<String>> myWords;
    // create only one and reuse it, so you get a truly random sequence of values
    private Random myRandom;


    /**
     * Create dictionary from file of words.
     */
    public HangmanDictionary (String wordFile) {
        myWords = loadFromFile(wordFile);
        myRandom = new Random();
    }

    /**
     * Return randomly chosen word of specified length, or empty string
     * if there are no words of that length.
     * 
     * @param wordLength is length of returned word
     * @return randomly chosen word (or "" if no words)
     */
    public String getRandomWord (int wordLength) {
        List<String> wordList = getWords(wordLength);
        // are there any words of this length left?
        return (wordList.size() > 0) ? wordList.get(myRandom.nextInt(wordList.size())) : "";
    }

    /**
     * Return all words of specified length, or empty list
     * if there are no words of that length.
     * 
     * @param wordLength is length of all returned words
     * @return list of words all of same length (or empty list)
     */
    public List<String> getWords (int wordLength) {
        return (myWords.containsKey(wordLength)) ? myWords.get(wordLength) : new ArrayList<>();
    }

    // load words from a file into map based on their length
    private Map<Integer, List<String>> loadFromFile (String filename) {
        Map<Integer, List<String>> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)))) {
            String line = reader.readLine();
            while (line != null) {
                String word = line.trim();
                List<String> words = result.getOrDefault(word.length(), new ArrayList<>());
                words.add(word);
                result.put(word.length(), words);
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            System.err.println("A error occurred reading word file: " + e);
        }
        return result;
    }
}
