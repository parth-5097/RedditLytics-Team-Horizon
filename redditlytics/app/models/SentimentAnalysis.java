package models;

import play.mvc.*;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class SentimentAnalysis {


    StanfordCoreNLP pipeline;

    public void SentimentAnalysis() {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and sentiment

    }

    public void getSentimentResult(String jsonBody) {
        System.out.println(jsonBody);

        try{
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
            pipeline = new StanfordCoreNLP(props);
            String text = null;
            String[] sliceData = jsonBody.split("%5097%");
            for (int i = 0; i < sliceData.length; i++) {
                Object object = new JSONParser().parse(sliceData[i]);
                JSONObject t = (JSONObject) object;
                if (text==null){
                    text =  ((String) t.get("selftext"));
                }else {
                    text+= ((String) t.get("selftext"));
                }
            }
            System.out.println("Text :"+text);


            // SentimentResult sentimentResult = new SentimentResult();
            //SentimentClassification sentimentClass = new SentimentClassification();
            String sentimentType = null;
            if (text != null && text.length() > 0) {

                Annotation annotation = pipeline.process(text);

                for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

                    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                    SimpleMatrix sm = RNNCoreAnnotations.getPredictions(tree);
                    sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

//                sentimentClass.setVeryPositive((double)Math.round(sm.get(4) * 100d));
//                sentimentClass.setPositive((double)Math.round(sm.get(3) * 100d));
//                sentimentClass.setNeutral((double)Math.round(sm.get(2) * 100d));
//                sentimentClass.setNegative((double)Math.round(sm.get(1) * 100d));
//                sentimentClass.setVeryNegative((double)Math.round(sm.get(0) * 100d));
//
//                sentimentResult.setSentimentScore(RNNCoreAnnotations.getPredictedClass(tree));
//                sentimentResult.setSentimentType(sentimentType);
//                sentimentResult.setSentimentClass(sentimentClass);
                }

            }

            System.out.println("sentime type -------------------- : "+sentimentType);

        }catch (Exception e){

        }

    }


}