package businesslogic;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SentimentAnalyzerTest {

    @Test
    public void findSentimentTest(){

        SentimentAnalyzer sc = new SentimentAnalyzer();
        sc.init();

        String positive = "High quality pants. Very comfortable and great for sport activities. Good price for nice quality! I recommend to all fans of sports";
        String negative = "The new design is awful!";

        assertEquals(0,sc.findSentiment(null));
        assertEquals(0,sc.findSentiment(""));
        assertEquals(3,sc.findSentiment(positive));
        assertEquals(1,sc.findSentiment(negative));
    }
}