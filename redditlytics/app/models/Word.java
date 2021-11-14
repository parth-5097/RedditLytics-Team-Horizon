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


/**
 * <p>The word class is defined to calculate the word statistics on the results of PushShift api.</p>
 * <p>The class has a method which receive a whole json string which will process in function and lemmitization
 * and tokenization is acheived using coreNLP stanford lib.</p>
 *
 * @author Jigar Borad
 */
public class Word {

    private String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";


    /**
     * <p>The function is designed to manipulae a stirng and perform lemmitization and spliting on string.</p>
     * <p>The string also going to be normalize in funtion.</p>
     * <p>the function also calculate the frequency of words in string paramater.</p>
     *
     * @param searchWord The Json string which will be used in function by CORENlp lib to manipulate data.
     * @return The list of <class>WordCount</class> objects are returned which contains the stirng word and its count value.
     */
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