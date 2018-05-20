import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
        HashSet<String> words = new HashSet<>();
        for (String word : p_given_pos.keySet())
            words.add(word);
        for (String word : p_given_neg.keySet())
            words.add(word);
        for (String word : p_given_not.keySet())
            words.add(word);
        int totalWordCount = words.size();

        // Finalize probabilities.
        for (String word : p_given_pos.keySet()) {
            p_given_pos.put(word, (p_given_pos.get(word) + 1) / (wordCountPos + totalWordCount));
        }

        for (String word : p_given_neg.keySet()) {
            p_given_neg.put(word, (p_given_neg.get(word) + 1) / (wordCountNeg + totalWordCount));
        }

        for (String word : p_given_not.keySet()) {
            p_given_not.put(word, (p_given_not.get(word) + 1) / (wordCountNot + totalWordCount));
        }
        // defines the length of feature selection in the positive, negative and notr sets.
        int limit = 50;
        HashSet<String> featureDictionary = MutualInformationHelper.getFeatures(p_given_pos,p_given_not,p_given_neg,limit);
        // TODO : use feature dictionary in classifying function ( IRMAK <3 thank youu)
        double p_notpresent_not = 1.0 / (wordCountNot + totalWordCount);
        double p_notpresent_neg = 1.0 / (wordCountNeg + totalWordCount);
        double p_notpresent_pos = 1.0 / (wordCountPos + totalWordCount);

        // Classify!
        classifySet(rotation.positiveTestSet, 1,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not);
        classifySet(rotation.negativeTestSet, -1,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not);
        classifySet(rotation.notrTestSet, 0,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not);
    }

    private static void classifySet(ArrayList<Tweet> testSet, int realClass,
                                    double p_class_pos, HashMap<String, Double> p_given_pos, double p_notpresent_pos,
                                    double p_class_neg, HashMap<String, Double> p_given_neg, double p_notpresent_neg,
                                    double p_class_not, HashMap<String, Double> p_given_not, double p_notpresent_not) {
        int classifiedPos = 0, classifiedNeg = 0, classifiedNot = 0;
        for (Tweet t : testSet) {
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
            double p_neg = Math.log10(p_class_neg);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_neg.containsKey(word)) {
                    p_neg += Math.log10(p_given_neg.get(word)) * t.getBagOfWords().get(word);
                } else {
                    p_neg += Math.log10(p_notpresent_neg);
                }
            }

            // Calculate notr probability.
            double p_not = Math.log10(p_class_not);
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
        System.out.println("Count\tpos\t\tneg\t\tnotr");
        System.out.println(testSet.size() + "\t\t" + classifiedPos + "\t\t" + classifiedNeg+ "\t\t" + classifiedNot);
    }
}