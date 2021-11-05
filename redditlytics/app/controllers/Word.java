package controllers;

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

class Word{

    private String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q";

    public List<Wordcount> bodyData(String searchWord){
        StanfordCoreNLP pipeline;
        JSONObject bodyData=null;
        String[] words;
        HttpResponse res= null;
        List<String> l= new ArrayList<>();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props, false);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI+URLEncoder.encode(searchWord,"UTF-8")+"&size=250&fields=selftext")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            JSONObject test = (JSONObject) obj;
            JSONArray array = (JSONArray) test.get("data");
            for (int i=0;i<array.size();i++){
                bodyData = (JSONObject) array.get(i);
                String a = ((String) bodyData.get("selftext"));
                a = a.replaceAll("[^a-zA-Z'0-9]+"," ").toLowerCase();
                Annotation document = pipeline.process(a);

                //System.out.println(a);
                for(CoreMap sentence: document.get(SentencesAnnotation.class))
                {
                    for(CoreLabel token: sentence.get(TokensAnnotation.class))
                    {
                        String word = token.get(TextAnnotation.class);
                        String lemma = token.get(LemmaAnnotation.class);
                        l.add(lemma);
                       // System.out.println("lemmatized version :" + lemma);
                    }
                }

            }

        }catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> distinct_word_list = l.stream()
                .distinct()
                .collect(Collectors.toList());
        ArrayList<Wordcount> uniquewords = new ArrayList<Wordcount>();
        for (String search:distinct_word_list) {
            uniquewords.add(new Wordcount(search,Collections.frequency(l, search)));
        }

        return uniquewords;
    }
}