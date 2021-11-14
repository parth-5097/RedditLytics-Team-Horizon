package models;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SentimentAnalyzerTest {

    @Test
    public void findSentimentTest(){

        SentimentAnalyzer sc = new SentimentAnalyzer();
        sc.init();

        String positive = "High quality pants. Very comfortable and great for sport activities. Good price for nice quality! I recommend to all fans of sports";
        String neutral = "I’m not sure if I like the new design";
        String negative = "The new design is awful!";

        assertEquals(0,sc.findSentiment(null));
        assertEquals(0,sc.findSentiment(""));
    }
}