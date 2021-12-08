package businesslogic;

import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import akka.actor.*;

/**
 * <p>The class is defined to calculate the sentiment result of given string using coreNLP lib.</p>
 *
 * @author Group Development
 */
public class SentimentAnalyzer extends AbstractActor{

    static StanfordCoreNLP pipeline;


    public static Props getProps() {
        return Props.create(SentimentAnalyzer.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, hello -> {
                    int temp = findSentiment(hello);
                    sender().tell(temp, self());
                })
                .build();
    }


    /**
     * <p>initalization of sentiment default objects.</p>
     */
    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }


    /**
     * <p>the functions perform analysis on string and calculate the score.</p>
     * <p>The function uses stanford coreNLP lib to calculate sentiment on string.</p>
     *
     * @param tweet The string parameter on which the sentiment analysis will be performed.
     * @return 0 - negative, 1- neutral, 2- positive
     */
    public static int findSentiment(String tweet) {
        init();
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);

            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                SimpleMatrix sentiment_new = RNNCoreAnnotations.getPredictions(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }
}