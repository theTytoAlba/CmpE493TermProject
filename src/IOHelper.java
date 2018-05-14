import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class IOHelper {

    /**
     * Reads tweets from given file.
     * The expected format is id\ttext.
     * Returns an ArrayList of Tweets.
     */
    public static ArrayList<Tweet> readTweets(String filePath, int tweetCategory) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", 2);
                tweets.add(new Tweet(parts[0].trim(), parts[1].trim(), tweetCategory));
            }
        } catch (Exception e) {
            System.out.println("Error while getting tweets from " + filePath);
        }
        return tweets;
    }

    /**
     * Reads words from given file.
     * The expected format is one word per line.
     * Returns an ArrayList of Strings.
     */
    public static ArrayList<String> readWords(String filePath) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error while getting words from " + filePath);
        }
        return words;
    }
}
