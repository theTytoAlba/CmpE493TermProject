import java.util.ArrayList;

public class Main {
    // Variables below are going to be set by readInformation function.
    static ArrayList<Tweet> positiveSet, negativeSet, notrSet;
    static ArrayList<String> positiveWords, negativeWords;

    public static void main(String[] args) {
        readInformation();
    }

    /**
     * Reads positive, negative and notr tweets.
     * Reads positive and negative words.
     */
    private static void readInformation() {
        // Read tweets.
        positiveSet = IOHelper.readTweets("Train/positive-train", 1);
        negativeSet = IOHelper.readTweets("Train/negative-train", -1);
        notrSet = IOHelper.readTweets("Train/notr-train", 0);
        System.out.println("Read "
                + positiveSet.size() + " positive, " + negativeSet.size() + " negative, " + notrSet.size() + " notr; "
                + "total " + (positiveSet.size() + negativeSet.size() + notrSet.size()) + " tweets.");
        // Read words.
        positiveWords = IOHelper.readWords("Train/positive-words");
        negativeWords = IOHelper.readWords("Train/negative-words");
        System.out.println("Read " + positiveWords.size() + " positive, " + negativeWords.size() + " negative words.");
    }
}
