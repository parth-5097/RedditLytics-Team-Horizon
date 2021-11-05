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

public class Word {

    private String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";

    public List<Wordcount> bodyData(String searchWord) {
        StanfordCoreNLP pipeline;
        String[] words;
        List<String> l = new ArrayList<>();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props, false);
        try {
            String[] sliceData = searchWord.split("%5097%");
            for (int i = 0; i < sliceData.length; i++) {
                Object object = new JSONParser().parse(sliceData[i]);
                JSONObject t = (JSONObject) object;
                String a = ((String) t.get("selftext"));
                a = a.replaceAll("[^a-zA-Z'0-9]+", " ").toLowerCase();
                Annotation document = pipeline.process(a);
                for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
                    for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                        String word = token.get(TextAnnotation.class);
                        String lemma = token.get(LemmaAnnotation.class);
                        l.add(lemma);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> distinct_word_list = l.stream()
                .distinct()
                .collect(Collectors.toList());
        ArrayList<Wordcount> uniquewords = new ArrayList<Wordcount>();
        for (String search : distinct_word_list) {
            uniquewords.add(new Wordcount(search, Collections.frequency(l, search)));
        }

        List<Wordcount> sortedWord = uniquewords.stream()
                .sorted(Comparator.comparingInt(Wordcount::getValue).reversed())
                .collect(Collectors.toList());
        return sortedWord;
    }
}