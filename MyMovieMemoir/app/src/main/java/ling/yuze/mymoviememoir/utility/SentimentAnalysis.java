package ling.yuze.mymoviememoir.utility;

public class SentimentAnalysis {
    private String[] positiveWords;
    private String[] negativeWords;
    private String sentence;

    public SentimentAnalysis (String[] wordsPositive, String[] wordsNegative) {
        positiveWords = wordsPositive;
        negativeWords = wordsNegative;
    }

    public void setSentence(String newSentence) {
        sentence = newSentence;
    }

    public int analyze() {
        int sentiment = 0;  //neutral by default

        int countPositive = 0;
        int countNegative = 0;



        if (countNegative > countPositive) return -1; // considered negative
        if (countNegative < countPositive) return 1; // considered positive

        return sentiment;
    }
}
