import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StatisticsHelper {

    public static void createPositiveNegativeWordStatistics(){

        HashMap<Tweet,Integer> positiveWordCount = new HashMap<>();
        HashMap<Tweet,Integer> negativeWordCount = new HashMap<>();

        HashMap<Tweet,Integer> positiveWordCountNegativeSet = new HashMap<>();
        HashMap<Tweet,Integer> negativeWordCountNegativeSet = new HashMap<>();


        HashMap<Tweet,Integer> positiveWordCountNotrSet = new HashMap<>();
        HashMap<Tweet,Integer> negativeWordCountNotrSet = new HashMap<>();
        int[][] stats = new int[3][4];
        /*
        stats[0] = positive set
        stats[1] = negative set
        stats[2] = notr set

        stats[][]                   not                     not
                        contains    contains    contains    contains
                        positive    positive    negative    negative
        positive set        -           -           -           -
        negative set        -           -           -           -
        notr set            -           -           -           -

        */
        int[][] tweetStats = new int[3][4];
        /*
                                            contains 1      contains 2      contains 3      contains more than 3
                                            positive word   positive word   positive word   positive word
        tweetStats[0] = positive set            -               -               -               -
        tweetStats[1] = negative set            -               -               -               -
        tweetStats[2] = notr set                -               -               -               -
         */
        for(Tweet t : Main.negativeSet){
            HashMap<String,Integer> map = t.getBagOfWords();
            boolean hasPositive = false;
            boolean hasNegative = false;
            for (String word: Main.positiveWords){
                if (map.containsKey(word)){
                    hasPositive = true;
                    if (positiveWordCountNegativeSet.containsKey(t)){
                        int k = positiveWordCountNegativeSet.get(t);
                        k+= map.get(word);
                        positiveWordCountNegativeSet.put(t,k);
                    }else{
                        positiveWordCountNegativeSet.put(t,map.get(word));
                    }
                }
            }
            if (hasPositive){
                stats[1][0] += 1;
            }else{
                stats[1][1] += 1;
            }
            //Search for negatives
            for (String word: Main.negativeWords){
                if (map.containsKey(word)){
                    hasNegative = true;
                    if (negativeWordCountNegativeSet.containsKey(t)){
                        int k = negativeWordCountNegativeSet.get(t);
                        k+= map.get(word);
                        negativeWordCountNegativeSet.put(t,k);
                    }else{
                        negativeWordCountNegativeSet.put(t,map.get(word));
                    }
                }
            }
            if (hasNegative){
                stats[1][2] += 1;
            }else{
                stats[1][3] += 1;
            }
        }
        for(Tweet t : Main.positiveSet){
            HashMap<String,Integer> map = t.getBagOfWords();
            boolean hasPositive = false;
            boolean hasNegative = false;
            for (String word: Main.positiveWords){
                if (map.containsKey(word)){
                    hasPositive = true;
                    if (positiveWordCount.containsKey(t)){
                        int k = positiveWordCount.get(t);
                        k+= map.get(word);
                        positiveWordCount.put(t,k);
                    }else{
                        positiveWordCount.put(t,map.get(word));
                    }
                }
            }
            if (hasPositive){
                stats[0][0] += 1;
            }else{
                stats[0][1] += 1;
            }
            //Search for negatives
            for (String word: Main.negativeWords){
                if (map.containsKey(word)){
                    hasNegative = true;
                    if (negativeWordCount.containsKey(t)){
                        int k = negativeWordCount.get(t);
                        k+= map.get(word);
                        negativeWordCount.put(t,k);
                    }else{
                        negativeWordCount.put(t,map.get(word));
                    }
                }
            }
            if (hasNegative){
                stats[0][2] += 1;
            }else{
                stats[0][3] += 1;
            }
        }

        for(Tweet t : Main.notrSet){
            HashMap<String,Integer> map = t.getBagOfWords();
            boolean hasPositive = false;
            boolean hasNegative = false;
            for (String word: Main.positiveWords){
                if (map.containsKey(word)){
                    hasPositive = true;
                    if (positiveWordCountNotrSet.containsKey(t)){
                        int k = positiveWordCountNotrSet.get(t);
                        k+= map.get(word);
                        positiveWordCountNotrSet.put(t,k);
                    }else{
                        positiveWordCountNotrSet.put(t,map.get(word));
                    }
                }
            }
            if (hasPositive){
                stats[2][0] += 1;
            }else{
                stats[2][1] += 1;
            }
            //Search for negatives
            for (String word: Main.negativeWords){
                if (map.containsKey(word)){
                    hasNegative = true;
                    if (negativeWordCountNotrSet.containsKey(t)){
                        int k = negativeWordCountNotrSet.get(t);
                        k+= map.get(word);
                        negativeWordCountNotrSet.put(t,k);
                    }else{
                        negativeWordCountNotrSet.put(t,map.get(word));
                    }
                }
            }
            if (hasNegative){
                stats[2][2] += 1;
            }else{
                stats[2][3] += 1;
            }
        }
        // positive word count
        for (Tweet t : positiveWordCount.keySet()){
            if (positiveWordCount.get(t) == 1 ){
                tweetStats[0][0] += 1;
            }else if (positiveWordCount.get(t) == 2){
                tweetStats[0][1] += 1;
            }else if (positiveWordCount.get(t) == 3){
                tweetStats[0][2] += 1;
            }else if (positiveWordCount.get(t) > 3){
                tweetStats[0][3] += 1;
            }
        }
        for (Tweet t : positiveWordCountNegativeSet.keySet()){
            if (positiveWordCountNegativeSet.get(t) == 1 ){
                tweetStats[0][0] += 1;
            }else if (positiveWordCountNegativeSet.get(t) == 2){
                tweetStats[0][1] += 1;
            }else if (positiveWordCountNegativeSet.get(t) == 3){
                tweetStats[0][2] += 1;
            }else if (positiveWordCountNegativeSet.get(t) > 3){
                tweetStats[0][3] += 1;
            }
        }
        for (Tweet t : positiveWordCountNotrSet.keySet()){
            if (positiveWordCountNotrSet.get(t) == 1 ){
                tweetStats[0][0] += 1;
            }else if (positiveWordCountNotrSet.get(t) == 2){
                tweetStats[0][1] += 1;
            }else if (positiveWordCountNotrSet.get(t) == 3){
                tweetStats[0][2] += 1;
            }else if (positiveWordCountNotrSet.get(t) > 3){
                tweetStats[0][3] += 1;
            }
        }
        //negative word count
        for (Tweet t : negativeWordCount.keySet()){
            if (negativeWordCount.get(t) == 1 ){
                tweetStats[1][0] += 1;
            }else if (negativeWordCount.get(t) == 2){
                tweetStats[1][1] += 1;
            }else if (negativeWordCount.get(t) == 3){
                tweetStats[1][2] += 1;
            }else if (negativeWordCount.get(t) > 3){
                tweetStats[1][3] += 1;
            }
        }
        for (Tweet t : negativeWordCountNegativeSet.keySet()){
            if (negativeWordCountNegativeSet.get(t) == 1 ){
                tweetStats[1][0] += 1;
            }else if (negativeWordCountNegativeSet.get(t) == 2){
                tweetStats[1][1] += 1;
            }else if (negativeWordCountNegativeSet.get(t) == 3){
                tweetStats[1][2] += 1;
            }else if (negativeWordCountNegativeSet.get(t) > 3){
                tweetStats[1][3] += 1;
            }
        }
        for (Tweet t : negativeWordCountNotrSet.keySet()){
            if (negativeWordCountNotrSet.get(t) == 1 ){
                tweetStats[1][0] += 1;
            }else if (negativeWordCountNotrSet.get(t) == 2){
                tweetStats[1][1] += 1;
            }else if (negativeWordCountNotrSet.get(t) == 3){
                tweetStats[1][2] += 1;
            }else if (negativeWordCountNotrSet.get(t) > 3){
                tweetStats[1][3] += 1;
            }
        }
        System.out.println("Stats : \t Cont.Pos \t NotCnt.Pos \t Cont.Neg \t NotCnt.Neg");
        for (int i = 0 ; i<3;i++){
            switch (i){
                case 0:
                    System.out.print("Positive Set\t");
                    break;
                case 1:
                    System.out.print("Negative Set\t");
                    break;
                case 2:
                    System.out.print("Notr Set\t\t");
                    break;
            }
            for (int j = 0; j < 4; j++) {
                System.out.print(stats[i][j] + "\t\t");
            }
            System.out.println();
        }
        System.out.println("Positive word count stats,Bir tweette bir tane pozitif kelime bulunanlar, iki tane olanlar, 3 tane olanlar 3 ve ustu..");
        System.out.println(Arrays.toString(tweetStats[0]));
        System.out.println("Negative word count stats,Bir tweette bir tane negative kelime bulunanlar, iki tane olanlar, 3 tane olanlar 3 ve ustu..");
        System.out.println(Arrays.toString(tweetStats[1]));

    }

    public static ArrayList<Double> posF = new ArrayList<>();
    public static ArrayList<Double> negF = new ArrayList<>();
    public static ArrayList<Double> notF = new ArrayList<>();

    public static ArrayList<Double> getFScores() {
        ArrayList<Double> res = new ArrayList();
        double sum = 0;
        for (double d : posF) {
            sum += d;
        }
        sum /= posF.size();
        res.add(sum);

        sum = 0;
        for (double d : negF) {
            sum += d;
        }
        sum /= negF.size();
        res.add(sum);

        sum = 0;
        for (double d : notF) {
            sum += d;
        }
        sum /= notF.size();
        res.add(sum);

        return res;
    }


}
