import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Classifier {

    /**
     * Calculates multinomial bayes for a all rotations.
     */
    public static void multinomialBayes(ArrayList<Rotation> rotations) {
        for (Rotation rotation : rotations) {
            multinomialBayes(rotation);
        }
        System.out.println("Avg f score " + StatisticsHelper.getFScores().toString());
        System.out.println("AVG " + (StatisticsHelper.getFScores().get(0) +
                StatisticsHelper.getFScores().get(1) +
                StatisticsHelper.getFScores().get(2)) / 3);
    }

    /**
     * Calculates multinomial bayes using all input data.
     */
    public static void multinomialBayes() {
        // Create class probabilities.
        double p_class_pos = Main.positiveSet.size()
                / (double) (Main.positiveSet.size() + Main.negativeSet.size() + Main.notrSet.size());
        double p_class_neg = Main.negativeSet.size()
                / (double) (Main.positiveSet.size() + Main.negativeSet.size() + Main.notrSet.size());
        double p_class_not = Main.notrSet.size()
                / (double) (Main.positiveSet.size() + Main.negativeSet.size() + Main.notrSet.size());

        // Create word probabilities for positive set.
        HashMap<String, Double> p_given_pos = new HashMap<>();
        int wordCountPos = 0;
        for (Tweet t : Main.positiveSet) {
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
        for (Tweet t : Main.negativeSet) {
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
        for (Tweet t : Main.notrSet) {
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
        int limit = 200;
        ArrayList<ArrayList<String>> featureDictionary = MutualInformationHelper.getFeatures(p_given_pos, p_given_not, p_given_neg, limit);

        double p_notpresent_not = 1.0 / (wordCountNot + totalWordCount);
        double p_notpresent_neg = 1.0 / (wordCountNeg + totalWordCount);
        double p_notpresent_pos = 1.0 / (wordCountPos + totalWordCount);

        // Classify!
        ArrayList<Integer> results = classifySet(Main.inputData,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not, featureDictionary);

        IOHelper.writeResults(Main.OUTPUT_DATA_FILE, results);
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
        int limit = 200;
        ArrayList<ArrayList<String>> featureDictionary = MutualInformationHelper.getFeatures(p_given_pos, p_given_not, p_given_neg, limit);

        double p_notpresent_not = 1.0 / (wordCountNot + totalWordCount);
        double p_notpresent_neg = 1.0 / (wordCountNeg + totalWordCount);
        double p_notpresent_pos = 1.0 / (wordCountPos + totalWordCount);

        // Classify!
        ArrayList<Integer> posRes = classifySet(rotation.positiveTestSet, 0,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not, featureDictionary);
        ArrayList<Integer> negRes = classifySet(rotation.negativeTestSet, 1,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not, featureDictionary);
        ArrayList<Integer> notRes = classifySet(rotation.notrTestSet, 2,
                p_class_pos, p_given_pos, p_notpresent_pos,
                p_class_neg, p_given_neg, p_notpresent_neg,
                p_class_not, p_given_not, p_notpresent_not, featureDictionary);

        double posP = posRes.get(0) / (double) (posRes.get(0) + (negRes.get(0) + notRes.get(0)));
        double posR = posRes.get(0) / (double) (posRes.get(0) + (posRes.get(1) + posRes.get(2)));

        double negP = negRes.get(1) / (double) (negRes.get(1) + (posRes.get(1) + notRes.get(1)));
        double negR = negRes.get(1) / (double) (negRes.get(1) + (negRes.get(0) + negRes.get(2)));

        double notP = notRes.get(2) / (double) (notRes.get(2) + (posRes.get(2) + negRes.get(2)));
        double notR = notRes.get(2) / (double) (notRes.get(2) + (notRes.get(0) + notRes.get(1)));

        StatisticsHelper.posF.add(2 * posP * posR / (posP + posR));
        StatisticsHelper.negF.add(2 * negP * negR / (negP + negR));
        StatisticsHelper.notF.add(2 * notP * notR / (notP + notR));
    }

    private static ArrayList<Integer> classifySet(ArrayList<Tweet> testSet,
                                                  double p_class_pos, HashMap<String, Double> p_given_pos, double p_notpresent_pos,
                                                  double p_class_neg, HashMap<String, Double> p_given_neg, double p_notpresent_neg,
                                                  double p_class_not, HashMap<String, Double> p_given_not, double p_notpresent_not, ArrayList<ArrayList<String>> featureDictionary) {
        int classifiedPos = 0, classifiedNeg = 0, classifiedNot = 0;
        ArrayList<Integer> result = new ArrayList<>();
        for (Tweet t : testSet) {
            // Calculate positive probability.
            double p_pos = Math.log10(p_class_pos);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_pos.containsKey(word)) {
                    p_pos +=
                            Math.log10(p_given_pos.get(word) + (featureDictionary.get(0).contains(word) || Main.positiveSet.contains(word) ?
                                    (1 - p_given_pos.get(word)) * 0.50 : 0))
                                    * t.getBagOfWords().get(word);
                } else {
                    p_pos += Math.log10(p_notpresent_pos);
                }
            }

            // Calculate negative probability.
            double p_neg = Math.log10(p_class_neg);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_neg.containsKey(word)) {
                    p_neg += Math.log10(p_given_neg.get(word) + (featureDictionary.get(1).contains(word) || Main.negativeSet.contains(word) ?
                            (1 - p_given_neg.get(word)) * 0.50 : 0)) * t.getBagOfWords().get(word);
                } else {
                    p_neg += Math.log10(p_notpresent_neg);
                }
            }

            // Calculate notr probability.
            double p_not = Math.log10(p_class_not);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_not.containsKey(word)) {
                    p_not += Math.log10(p_given_not.get(word) + (featureDictionary.get(2).contains(word) ?
                            (1 - p_given_not.get(word)) * 0.50 : 0)) * t.getBagOfWords().get(word);
                } else {
                    p_not += Math.log10(p_notpresent_not);
                }
            }

            // Classify
            if (p_pos > p_neg && p_pos > p_not) {
                result.add(1);
                classifiedPos++;
            } else if (p_neg > p_not) {
                result.add(-1);
                classifiedNeg++;
            } else {
                result.add(0);
                classifiedNot++;
            }
        }
        System.out.println("Classified " + classifiedPos + " as positive, " + classifiedNeg + " as negative, " + classifiedNot + " as notr.");
        return result;
    }

    private static ArrayList<Integer> classifySet(ArrayList<Tweet> testSet, int realClass,
                                                  double p_class_pos, HashMap<String, Double> p_given_pos, double p_notpresent_pos,
                                                  double p_class_neg, HashMap<String, Double> p_given_neg, double p_notpresent_neg,
                                                  double p_class_not, HashMap<String, Double> p_given_not, double p_notpresent_not, ArrayList<ArrayList<String>> featureDictionary) {
        int classifiedPos = 0, classifiedNeg = 0, classifiedNot = 0;
        for (Tweet t : testSet) {
            // Calculate positive probability.
            double p_pos = Math.log10(p_class_pos);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_pos.containsKey(word)) {
                    p_pos +=
                            Math.log10(p_given_pos.get(word) + (featureDictionary.get(0).contains(word) || Main.positiveSet.contains(word) ?
                                    (1 - p_given_pos.get(word)) * 0.50 : 0))
                                    * t.getBagOfWords().get(word);
                } else {
                    p_pos += Math.log10(p_notpresent_pos);
                }
            }

            // Calculate negative probability.
            double p_neg = Math.log10(p_class_neg);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_neg.containsKey(word)) {
                    p_neg += Math.log10(p_given_neg.get(word) + (featureDictionary.get(1).contains(word) || Main.negativeSet.contains(word) ?
                            (1 - p_given_neg.get(word)) * 0.50 : 0)) * t.getBagOfWords().get(word);
                } else {
                    p_neg += Math.log10(p_notpresent_neg);
                }
            }

            // Calculate notr probability.
            double p_not = Math.log10(p_class_not);
            for (String word : t.getBagOfWords().keySet()) {
                if (p_given_not.containsKey(word)) {
                    p_not += Math.log10(p_given_not.get(word) + (featureDictionary.get(2).contains(word) ?
                            (1 - p_given_not.get(word)) * 0.50 : 0)) * t.getBagOfWords().get(word);
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
        ArrayList<Integer> res = new ArrayList<>();
        res.add(classifiedPos);
        res.add(classifiedNeg);
        res.add(classifiedNot);
        return res;
        //System.out.println("Count\tpos\t\tneg\t\tnotr");
        //System.out.println(testSet.size() + "\t\t" + classifiedPos + "\t\t" + classifiedNeg+ "\t\t" + classifiedNot);
    }
}