import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;

import java.io.IOException;
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
        // We should delete words after ' sign.
        //text = text.replaceAll("'", " ");
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


        WordAnalysis results;
        SingleAnalysis singleAnalysis;

        for (String token : text.split(" ")) {

            // If token contains ' sign, delete the words come after that sign.
            if (token.contains("'")){
                int index = token.indexOf("'");
                token = token.substring(0,index);
            }

            // Only accept tokens which are at least 2 chars, not integers and not twitter handles.
            if (!token.isEmpty() && token.length() > 1 && !isInteger(token) && token.charAt(0) != '@' && token.charAt(0)!= '#' && !isStopword(token)) {
                results = Main.morphology.analyze(token.trim());
                if (results.getAnalysisResults().size()!=0){
                    singleAnalysis = results.getAnalysisResults().get(0);
                    tokens.add(singleAnalysis.getStem());
                }else {
                    tokens.add(token.trim()); // buraya stem fonksiyonu gomecegiz
                }

            }
        }
        return tokens;
    }

    /**
     *
     * @param token
     * @return Checks if token in stopword list,
     * if it is a stopword returns true
     */
    private static boolean isStopword(String token) {
        if (Main.stopwords.contains(token)){
            return true;
        }else {
            return false;
        }
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
