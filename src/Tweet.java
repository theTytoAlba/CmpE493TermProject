import java.util.ArrayList;
import java.util.HashMap;

public class Tweet {
    String id;
    String text;
    int category; // 1: positive, 0: notr, -1: negative
    private HashMap<String, Integer> bagOfWords; // Only created when it is needed.

    public Tweet (String id, String text, int category) {
        this.id = id;
        this.text = text;
        this.category = category;
    }

    /**
     * Returns the bagOfWords map. Creates it if not created yet.
     * Map is in form <word: # of times or occurs>
     */
    public HashMap<String, Integer> getBagOfWords() {
        // If bag of words is not calculated yet, calculate it.
        if (bagOfWords == null) {
            bagOfWords = new HashMap<>();
            ArrayList<String> tokenizedText = TextProcessingHelper.tokenizeText(text);
            for (String token : tokenizedText) {
                if (bagOfWords.containsKey(token)) {
                    bagOfWords.put(token, bagOfWords.get(token) + 1);
                } else {
                    bagOfWords.put(token, 1);
                }
            }
        }
        return bagOfWords;
    }

    /**
     * Creates and returns a deep copy of this tweet.
     */
    public Tweet getCopy() {
        Tweet copy = new Tweet(id, text, category);
        copy.bagOfWords = new HashMap<>();
        for (String word : getBagOfWords().keySet()) {
            copy.bagOfWords.put(word, bagOfWords.get(word));
        }
        return copy;
    }
}
