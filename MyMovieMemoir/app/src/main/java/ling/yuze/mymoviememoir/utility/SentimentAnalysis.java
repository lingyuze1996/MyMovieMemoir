package ling.yuze.mymoviememoir.utility;

public class SentimentAnalysis {
    private String[] positiveWords;
    private String[] negativeWords;

    public SentimentAnalysis (String[] wordsPositive, String[] wordsNegative) {
        positiveWords = wordsPositive;
        negativeWords = wordsNegative;
    }

    public int analyze(String content) {
        int sentiment = 0;  //neutral by default

        int countPositive = 0;
        int countNegative = 0;
        String[] cuts = content.split("`|~|!|@|#|%|&|=|;|\"|:|\\?|<|>|\\.|,|\\s+");

        for (String cut : cuts) {
            for (String positive : positiveWords) {
                if (cut.equals(positive)) countPositive ++;
            }
            for (String negative : negativeWords) {
                if (cut.equals(negative)) countNegative ++;
            }
        }

        if (countNegative > countPositive) return -1; // considered negative
        if (countNegative < countPositive) return 1; // considered positive

        return sentiment;
    }
}
