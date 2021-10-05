package hangman;

import java.util.List;

public class test {
    public static void main(String[] args) {
        String pattern = "_ll_";
        String word = "alll";
        System.out.println(charMaxOccurrence(pattern, word));
    }

    // https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
    public static int countOccurrencesChar (String word, String localLetter) {
        int count = 0;
        if (Character.isAlphabetic(localLetter.charAt(0))){
            String wordCopy = word.replace(localLetter, "");
            count = word.length() - wordCopy.length();
        }
        else {
            count = 0;
        }
        return count;
    }

    public static int charMaxOccurrence(String wordDisplayPattern, String word) {
        int max = 0;

        for (int i = 0; i < wordDisplayPattern.length(); i++) {
            String localLetter = wordDisplayPattern.substring(i, i+1);
            int currentCount = countOccurrencesChar(word, localLetter);
            if (currentCount > max) {
                max = currentCount;
            }
        }
        return max;
    }
}
