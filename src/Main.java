import zemberek.core.logging.Log;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.AnalysisFormatters;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    // Variables below are going to be set by readInformation function.
    public static ArrayList<Tweet> positiveSet, negativeSet, notrSet, inputData;
    public static ArrayList<String> positiveWords, negativeWords;
    public static HashSet<String> stopwords;
    public static TurkishMorphology morphology;

    public static final String INPUT_DATA_FILE = "input.txt";
    public static final String OUTPUT_DATA_FILE = "output.txt";

    public static void main(String[] args) {
        readInformation();
        readStopwords();
        createMorphology();
        ArrayList<Rotation> rotations = createRotations();
        StatisticsHelper.createPositiveNegativeWordStatistics();
        Classifier.multinomialBayes(rotations);
        if (new File(INPUT_DATA_FILE).exists()){
            Classifier.multinomialBayes();
        }
    }



    /**
     * Zemberek Library
     * For Turkish morphological analysis
     * https://drive.google.com/open?id=1JAz-ZuYr7bJtYnUid0wq4H0BrgCHbNz3
     * from the link above you can download the library
     *
     * To add to IntelliJ platform, from Project Structure -> Modules -> Dependencies
     * add zemberek-all.jar and all dependency jar files, adding module-jars is not necessary.
     */
    private static void createMorphology() {
        try {
            morphology  = TurkishMorphology.createWithDefaults();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Divides the existing set into 11 sets.
     *
     * 10 sets contain:
     * - 171 positive test,
     * - 102 negative test,
     * - 307 notr test,
     * - 1708 positive train,
     * - 1019 negative train,
     * - 3065 notr train tweets.
     *
     * 11th set contains:
     * - 169 positive test,
     * - 101 negative test,
     * - 302 notr test,
     * - 1710 positive train,
     * - 1020 negative train,
     * - 3070 notr train tweets.
     *
     * Returns an ArrayList of Rotation objects.
     */
    private static ArrayList<Rotation> createRotations() {
        ArrayList<Rotation> rotations = new ArrayList<>();
        // Create first 10 sets.
        for (int i = 0; i < 10; i++) {
            // Handle positive tweets.
            ArrayList<Tweet> positiveTest = new ArrayList<>();
            ArrayList<Tweet> positiveTrain = new ArrayList<>();
            for (int j = 0; j < positiveSet.size(); j++) {
                // Collect 171 tweets as test and others as training.
                if (j >= i*171 && j < i*171+171) {
                    positiveTest.add(positiveSet.get(j).getCopy());
                } else {
                    positiveTrain.add(positiveSet.get(j).getCopy());
                }
            }
            // Handle negative tweets.
            ArrayList<Tweet> negativeTest = new ArrayList<>();
            ArrayList<Tweet> negativeTrain = new ArrayList<>();
            for (int j = 0; j < negativeSet.size(); j++) {
                // Collect 102 tweets as test and others as training.
                if (j >= i*102 && j < i*102+102) {
                    negativeTest.add(negativeSet.get(j).getCopy());
                } else {
                    negativeTrain.add(negativeSet.get(j).getCopy());
                }
            }
            // Handle notr tweets.
            ArrayList<Tweet> notrTest = new ArrayList<>();
            ArrayList<Tweet> notrTrain = new ArrayList<>();
            for (int j = 0; j < notrSet.size(); j++) {
                // Collect 307 tweets as test and others as training.
                if (j >= i*307 && j < i*307+307) {
                    notrTest.add(notrSet.get(j).getCopy());
                } else {
                    notrTrain.add(notrSet.get(j).getCopy());
                }
            }
            // Create rotation with these values and add to the list.
            Rotation rotation = new Rotation();
            rotation.positiveTestSet = positiveTest;
            rotation.positiveTrainSet = positiveTrain;
            rotation.negativeTestSet = negativeTest;
            rotation.negativeTrainSet = negativeTrain;
            rotation.notrTestSet = notrTest;
            rotation.notrTrainSet = notrTrain;
            rotations.add(rotation);
        }
        // Create the 11th set.
        // Handle positive tweets.
        ArrayList<Tweet> positiveTest = new ArrayList<>();
        ArrayList<Tweet> positiveTrain = new ArrayList<>();
        for (int j = 0; j < positiveSet.size(); j++) {
            // Collect 169 tweets as test and others as training.
            if (j >= 10*171 && j < 10*171+169) {
                positiveTest.add(positiveSet.get(j).getCopy());
            } else {
                positiveTrain.add(positiveSet.get(j).getCopy());
            }
        }
        // Handle negative tweets.
        ArrayList<Tweet> negativeTest = new ArrayList<>();
        ArrayList<Tweet> negativeTrain = new ArrayList<>();
        for (int j = 0; j < negativeSet.size(); j++) {
            // Collect 101 tweets as test and others as training.
            if (j >= 10*102 && j < 10*102+101) {
                negativeTest.add(negativeSet.get(j).getCopy());
            } else {
                negativeTrain.add(negativeSet.get(j).getCopy());
            }
        }
        // Handle notr tweets.
        ArrayList<Tweet> notrTest = new ArrayList<>();
        ArrayList<Tweet> notrTrain = new ArrayList<>();
        for (int j = 0; j < notrSet.size(); j++) {
            // Collect 302 tweets as test and others as training.
            if (j >= 10*307 && j < 10*307+302) {
                notrTest.add(notrSet.get(j).getCopy());
            } else {
                notrTrain.add(notrSet.get(j).getCopy());
            }
        }
        // Create rotation with these values and add to the list.
        Rotation rotation = new Rotation();
        rotation.positiveTestSet = positiveTest;
        rotation.positiveTrainSet = positiveTrain;
        rotation.negativeTestSet = negativeTest;
        rotation.negativeTrainSet = negativeTrain;
        rotation.notrTestSet = notrTest;
        rotation.notrTrainSet = notrTrain;
        rotations.add(rotation);
        System.out.println("Created 10 Rotations with "
                + rotations.get(0).positiveTrainSet.size() + " positive train, "
                + rotations.get(0).negativeTrainSet.size() + " negative train, "
                + rotations.get(0).notrTrainSet.size() + " notr train; "
                + rotations.get(0).positiveTestSet.size() + " positive test, "
                + rotations.get(0).negativeTestSet.size() + " negative test, "
                + rotations.get(0).notrTestSet.size() + " notr test tweet data.");

        System.out.println("Created 1 Rotation with "
                + rotations.get(10).positiveTrainSet.size() + " positive train, "
                + rotations.get(10).negativeTrainSet.size() + " negative train, "
                + rotations.get(10).notrTrainSet.size() + " notr train; "
                + rotations.get(10).positiveTestSet.size() + " positive test, "
                + rotations.get(10).negativeTestSet.size() + " negative test, "
                + rotations.get(10).notrTestSet.size() + " notr test tweet data.");
        return rotations;
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

        // Uncomment if you want to test.
        //IOHelper.writeTweets(INPUT_DATA_FILE, notrSet);

        System.out.println("Read "
                + positiveSet.size() + " positive, " + negativeSet.size() + " negative, " + notrSet.size() + " notr; "
                + "total " + (positiveSet.size() + negativeSet.size() + notrSet.size()) + " tweets.");
        // Read words.
        positiveWords = IOHelper.readWords("Train/positive-words");
        negativeWords = IOHelper.readWords("Train/negative-words");
        System.out.println("Read " + positiveWords.size() + " positive, " + negativeWords.size() + " negative words.");

        if (new File(INPUT_DATA_FILE).exists()){
            readInput();
        }
    }

    /**
     * Reads input file given in demo session
     */
    private static void readInput(){
        inputData = IOHelper.readInput(INPUT_DATA_FILE);
        System.out.println("Input data has been retrieved");
    }

    /**
     * Reads stopwords which needs to be removed in preprocessing phase
     */
    private static void readStopwords(){
        stopwords = IOHelper.readWordsHashSet("Train/stopwords-tr.txt");
    }
}
