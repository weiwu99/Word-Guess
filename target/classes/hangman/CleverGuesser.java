package hangman;

import util.HangmanDictionary;

import java.util.*;

public class CleverGuesser extends Guesser {
    // letters to guess
    private String myLetters;

    private List<String> wordList;

    private HashMap<String, List<String>> letterFrequency;

    private String alphabet;

    public CleverGuesser(HangmanDictionary dictionary, int wordLength, int numGuesses) {
        super(numGuesses);
        myLetters = HangmanGame.LETTERS_ORDERED_BY_FREQUENCY;

        wordList = dictionary.getWords(wordLength);
//        myLetters = "";
        letterFrequency = new HashMap<String, List<String>>();
        alphabet = HangmanGame.ALPHABET;
    }

    /**
     * Special guess for clever guesser
     * find the most frequent letter
     * @param keeper
     * @return
     */
    @Override
    public String cleverGuess(Keeper keeper) {
        // store each letter's coverage of the possible words into a hashmap
        letterFrequency = updateFreqMap(alphabet, wordList);

        int maxSize = 0;
        String maxLetter = "";

        maxLetter = getMaxLetter(maxSize, maxLetter);

        alphabet = updateAlphabet(maxLetter, alphabet, keeper);

        // narrow down scope:
        if (isCorrectGuess(maxLetter, keeper)) {
            wordList = updateList(maxLetter, letterFrequency, keeper);
        }

        return maxLetter;
    }

    /**
     * get the letter w/ maximal word occurrences
     * @param maxSize
     * @param maxLetter
     * @return
     */
    private String getMaxLetter(int maxSize, String maxLetter) {
        for (String key: letterFrequency.keySet()) {
            int currentSize = letterFrequency.get(key).size();
            if (currentSize >= maxSize) {
                maxSize = currentSize;
                maxLetter = key;
            }
        }
        return maxLetter;
    }

    /**
     * udpate the alphabet after a guess
     * Remove the letter if successful
     * Nothing if incorrect guess
     * @param maxLetter
     * @param letters
     * @return
     */
    private String updateAlphabet(String maxLetter, String letters, Keeper keeper) {
        StringBuilder alphaB = new StringBuilder();
        alphaB.append(letters);

        int maxIndex = alphaB.indexOf(maxLetter);
        alphaB.deleteCharAt(maxIndex);
        return alphaB.toString();
    }

    /**
     * Update the list with words in the correct position as well
     * @param maxLetter
     * @param letterMap
     * @return
     */
    private List<String> updateList(String maxLetter, HashMap<String, List<String>> letterMap, Keeper keeper) {

        // get info on display:
        String wordDisplayPattern = keeper.getMyDisplayWord().toString().replaceAll("\\s", ""); // wordDisplayPattern: key to record the pattern
        List<String> remainingWordList = letterMap.get(maxLetter);
        List<String> outputList = new ArrayList<>();

        checkPattern(wordDisplayPattern, remainingWordList, outputList);

        return outputList;
    }

    private void checkPattern(String wordDisplayPattern, List<String> remainingWordList, List<String> outputList) {
        for (int i = 0; i < remainingWordList.size(); i++) {
            int numSamePattern = 0;
            int numBlank = 0;

            String currentWord = remainingWordList.get(i);
            for (int j = 0; j < remainingWordList.get(i).length(); j++) {
                String localLetter = wordDisplayPattern.substring(j, j+1);
                String wordLetter = currentWord.substring(j,j+1);

                numBlank = countNumCondition(numBlank, !Character.isAlphabetic(localLetter.charAt(0))); // count "_"

                numSamePattern = countNumAlphabet(numSamePattern, localLetter, wordLetter); // count letters
            }

            if (numSamePattern == currentWord.length() - numBlank) {
                outputList.add(currentWord);
            }
        }
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

    private boolean isCorrectGuess(String maxLetter, Keeper keeper) {
        return keeper.getMySecretWord().contains(maxLetter);
    }

    private HashMap updateFreqMap(String alphabet, List<String> list) {
        HashMap<String, List<String>> letterMap = new HashMap<String, List<String>>();
        // loop thru the entire alphabet
        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.substring(i, i+1);
            letterMap.putIfAbsent(letter, new ArrayList<>());

            if (letterMap.get(letter) != null) {

                // loop thru the entire wordList:
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).contains(letter)) {
                        letterMap.get(letter).add(list.get(j));
                    }
                }
            }
        }

        return letterMap;
    }

}

