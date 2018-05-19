import java.util.ArrayList;
import java.util.regex.Pattern;

public class TextProcessingHelper {

    /**
     * Removes punctuation.
     * Removes twitter handles (for example, @twitterhandle does not have a weight on category decision).
     */
    public static ArrayList<String> tokenizeText(String text) {
        // Make text lowercase.
        text = text.toLowerCase();
        // Remove all punctuation marks and new lines.
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll(",", " ");
        text = text.replaceAll("'", " ");
        text = text.replaceAll("\"", " ");
        text = text.replaceAll("/", " ");
        text = text.replaceAll("-", " ");
        text = text.replaceAll("_", " ");
        text = text.replaceAll("\\*", " ");
        text = text.replaceAll("<", " ");
        text = text.replaceAll(">", " ");
        text = text.replaceAll(Pattern.quote("!"), " ");
        text = text.replaceAll(Pattern.quote("?"), " ");
        text = text.replaceAll(Pattern.quote(";"), " ");
        text = text.replaceAll(Pattern.quote(":"), " ");
        text = text.replaceAll(Pattern.quote("("), " ");
        text = text.replaceAll(Pattern.quote(")"), " ");
        text = text.replaceAll(Pattern.quote("="), " ");
        text = text.replaceAll(Pattern.quote("$"), " ");
        text = text.replaceAll(Pattern.quote("%"), " ");
        //text = text.replaceAll(Pattern.quote("#"), " ");
        text = text.replaceAll(Pattern.quote("+"), " ");
        text = text.replaceAll("\n", " ");
        // Tokenize by space.
        ArrayList<String> tokens = new ArrayList<>();
        for (String token : text.split(" ")) {
            // Only accept tokens which are at least 2 chars, not integers and not twitter handles.
            if (!token.isEmpty() && token.length() > 1 && !isInteger(token) && token.charAt(0) != '@' && token.charAt(0)!= '#') {
                tokens.add(token.trim()); // buraya stem fonksiyonu gomecegiz
            }
        }
        return tokens;
    }

    /**
     * Tries to cast given string to an integer.
     * Returns true if it succeeds, false otherwise.
     */
    private static boolean isInteger(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
