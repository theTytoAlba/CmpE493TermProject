import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

public class MutualInformationHelper {

    /**
     * Creates Featured Word list for positive, negative and notr sets
     * @param p_given_pos given probability of positive word Hashmap
     * @param p_given_not given probability of notr word Hashmap
     * @param p_given_neg given probability of negative word Hashmap
     * @param LIMIT integer value, look up value, i.e. Limit = 50, checks top 50 words having highest probabilities.
     * @return List of featured words. 
     */
    public static ArrayList<ArrayList<String>> getFeatures(HashMap<String, Double> p_given_pos, HashMap<String, Double> p_given_not, HashMap<String, Double> p_given_neg, int LIMIT) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();


        ArrayList<String> pos_features = new ArrayList<>();
        ArrayList<String> neg_features = new ArrayList<>();
        ArrayList<String> not_features = new ArrayList<>();
        //First calculate positive features versus non positive(notr and negative)

        List<Map.Entry<String, Double>> sorted_map_pos =
                p_given_pos.entrySet()
                        .stream()
                        .sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList());

        List<Map.Entry<String, Double>> sorted_map_neg =
                p_given_neg.entrySet()
                        .stream()
                        .sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList());
        List<Map.Entry<String, Double>> sorted_map_not =
                p_given_not.entrySet()
                        .stream()
                        .sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList());


        ArrayList<String> pos_fifty = new ArrayList<>();
        ArrayList<String> neg_fifty = new ArrayList<>();
        ArrayList<String> not_fifty = new ArrayList<>();

        for (int i = 0; i < LIMIT; i++) {
            pos_fifty.add(sorted_map_pos.get(i).getKey());
            neg_fifty.add(sorted_map_neg.get(i).getKey());
            not_fifty.add(sorted_map_not.get(i).getKey());
        }

        for (int i = 0; i < LIMIT; i++) {
            if (!neg_fifty.contains(pos_fifty.get(i)) && !not_fifty.contains(pos_fifty.get(i))) {
                pos_features.add(pos_fifty.get(i));
            }
            if (!pos_fifty.contains(neg_fifty.get(i)) && !not_fifty.contains(neg_fifty.get(i))) {
                neg_features.add(neg_fifty.get(i));
            }
            if (!pos_fifty.contains(not_fifty.get(i)) && !neg_fifty.contains(not_fifty.get(i))) {
                not_features.add(not_fifty.get(i));
            }

        }

        /**
         * Prints featured words in given sets.
         *

            System.out.println("POSITIVE FEATURE LIST " + pos_features.size());

            for (int i = 0; i < pos_features.size(); i++) {

                System.out.println(pos_features.get(i));

                result.add(pos_features.get(i));
            }

            System.out.println("\nNEGATIVE FEATURE LIST " + neg_features.size());
            for (int i = 0; i < neg_features.size(); i++) {
                System.out.println(neg_features.get(i));
                result.add(neg_features.get(i));
            }

            System.out.println("\nNOTR FEATURE LIST " + not_features.size());
            for (int i = 0; i < not_features.size(); i++) {
                System.out.println(not_features.get(i));
                result.add(not_features.get(i));
            }

         */
        result.add(pos_features);
        result.add(neg_features);
        result.add(not_features);

        return result;
    }

}
