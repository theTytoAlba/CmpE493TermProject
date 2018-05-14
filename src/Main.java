import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Read tweets.
        ArrayList<Tweet> positiveSet = IOHelper.readTweets("Train/positive-train", 1);
        ArrayList<Tweet> negativeSet = IOHelper.readTweets("Train/negative-train", -1);
        ArrayList<Tweet> notrSet = IOHelper.readTweets("Train/notr-train", 0);
    }
}
