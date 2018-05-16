import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Classifier {

    /**
     * Calculates multinomial bayes for a all rotations.
     */
    public static void multinomialBayes(ArrayList<Rotation> rotations) {
        for (Rotation rotation : rotations) {
            multinomialBayes(rotation);
        }
    }

    /**
     * Calculates multinomial bayes for a single rotation.
     */
    public static void multinomialBayes(Rotation rotation) {
        // Create class probabilities.
        double p_class_pos = rotation.positiveTrainSet.size()
                / (double) (rotation.positiveTrainSet.size() + rotation.negativeTrainSet.size() + rotation.notrTrainSet.size());
        double p_class_neg = rotation.negativeTrainSet.size()
                / (double) (rotation.positiveTrainSet.size() + rotation.negativeTrainSet.size() + rotation.notrTrainSet.size());
        double p_class_not = rotation.notrTrainSet.size()
                / (double) (rotation.positiveTrainSet.size() + rotation.negativeTrainSet.size() + rotation.notrTrainSet.size());

        // Create word probabilities for positive set.
        HashMap<String, Double> p_given_pos = new HashMap<>();
        int wordCountPos = 0;
        for (Tweet t : rotation.positiveTrainSet) {
            for (String word : t.getBagOfWords().keySet()) {
                // Update word count.
                wordCountPos += t.getBagOfWords().get(word);
                // Update word count of this word.
                if (p_given_pos.containsKey(word)) {
                    p_given_pos.put(word, p_given_pos.get(word) + t.getBagOfWords().get(word));
                } else {
                    p_given_pos.put(word, (double) t.getBagOfWords().get(word));
                }
            }
        }

        // Create word probabilities for negative set.
        HashMap<String, Double> p_given_neg = new HashMap<>();
        int wordCountNeg = 0;
        for (Tweet t : rotation.negativeTrainSet) {
            for (String word : t.getBagOfWords().keySet()) {
                // Update word count.
                wordCountNeg += t.getBagOfWords().get(word);
                // Update word count of this word.
                if (p_given_neg.containsKey(word)) {
                    p_given_neg.put(word, p_given_neg.get(word) + t.getBagOfWords().get(word));
                } else {
                    p_given_neg.put(word, (double) t.getBagOfWords().get(word));
                }
            }
        }

        // Create word probabilities for notr set.
        HashMap<String, Double> p_given_not = new HashMap<>();
        int wordCountNot = 0;
        for (Tweet t : rotation.notrTrainSet) {
            for (String word : t.getBagOfWords().keySet()) {
                // Update word count.
                wordCountNot += t.getBagOfWords().get(word);
                // Update word count of this word.
                if (p_given_not.containsKey(word)) {
                    p_given_not.put(word, p_given_not.get(word) + t.getBagOfWords().get(word));
                } else {
                    p_given_not.put(word, (double) t.getBagOfWords().get(word));
                }
            }
        }

        // Find total unique word count.
        Set<String> words = p_given_pos.keySet();
        words.addAll(p_given_neg.keySet());
        words.addAll(p_given_not.keySet());
        int totalWordCount = words.size();

        // Finalize probabilities.
        for (String word : p_given_pos.keySet()) {
            p_given_pos.put(word, (p_given_pos.get(word) + 1) / (wordCountPos + totalWordCount));
        }
        double p_notpresent_pos = 1.0 / (wordCountPos + totalWordCount);

        for (String word : p_given_neg.keySet()) {
            p_given_neg.put(word, (p_given_neg.get(word) + 1) / (wordCountNeg + totalWordCount));
        }
        double p_notpresent_neg = 1.0 / (wordCountNeg + totalWordCount);

        for (String word : p_given_not.keySet()) {
            p_given_not.put(word, (p_given_not.get(word) + 1) / (wordCountNot + totalWordCount));
        }
        double p_notpresent_not = 1.0 / (wordCountNot + totalWordCount);

        // Classify!
        int classifiedPos = 0, classifiedNeg = 0, classifiedNot = 0;
        for (Tweet t : rotation.positiveTestSet) {
            // Calculate positive probability.
            double p_pos = Math.log10(p_class_pos);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_pos.containsKey(word)) {
                    p_pos += Math.log10(p_given_pos.get(word)) * t.getBagOfWords().get(word);
                } else {
                    p_pos += Math.log10(p_notpresent_pos);
                }
            }

            // Calculate negative probability.
            double p_neg = Math.log10(p_class_pos);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_neg.containsKey(word)) {
                    p_neg += Math.log10(p_given_neg.get(word)) * t.getBagOfWords().get(word);
                } else {
                    p_neg += Math.log10(p_notpresent_neg);
                }
            }

            // Calculate notr probability.
            double p_not = Math.log10(p_class_pos);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_not.containsKey(word)) {
                    p_not  += Math.log10(p_given_not.get(word)) * t.getBagOfWords().get(word);
                } else {
                    p_not += Math.log10(p_notpresent_not);
                }
            }

            if (p_pos > p_neg && p_pos > p_not) {
                classifiedPos++;
            } else if (p_neg > p_not) {
                classifiedNeg++;
            } else {
                classifiedNot++;
            }
        }
        System.out.println("Within " + rotation.positiveTestSet.size() + " test tweets,");
        System.out.println(classifiedPos + " tweets are classified as positive and "
                + classifiedNeg + " tweets are classified as negative and "
                + classifiedNot + " tweets are classified as notr");
    }
}