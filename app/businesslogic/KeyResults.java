package businesslogic;

import play.mvc.*;
import models.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import play.api.libs.json.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.inject.Inject;
import akka.actor.*;
import akka.japi.*;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * <p>The class is defined to call PushShift api.</p>
 *
 * <p>The class has 3 methods. A) getData(string) b)subredditAPI(string) c)getSubredditData(string) </p>
 * <p>The methods are used to call api to search data of specific word or to search latest submission of subreddit</p>
 *
 */

public class KeyResults extends AbstractActor{
    String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";
    //    String subRedditAPI = "https://api.pushshift.io/reddit/search/submission/?subreddit=";
    HttpResponse res = null;
    JSONObject bodyData = null;


//    public static class Key{
//        public final String name;
//
//        public Key(String name){
//            this.name = name;
//        }
//    }


//    public static class SubredditKey{
//        public final String name;
//
//        public SubredditKey(String name){
//            this.name = name;
//        }
//    }

    public static Props getProps() {
        return Props.create(KeyResults.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, search -> {
                    String data = getData(search);
                    sender().tell(data, self());
                })
                .build();
    }

    /**
     * The functions trying to find out 250 latest submssions of the perticualr word using PushShift api.
     *
     * @author Group development
     * @param V The string parameter is used in PushShift api to search the results of that parameter.
     * @return The funcion returns the JSON String data.
     */
    public String getData(String V) {
        String a = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(V, "UTF-8") + "&size=250&fields=title,selftext,created_utc,author,subreddit&sort=DESC")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            JSONObject test = (JSONObject) obj;
            JSONArray array = (JSONArray) test.get("data");
            for (int i = 0; i < array.size(); i++) {
                bodyData = (JSONObject) array.get(i);
                if (a == null) {
                    a = bodyData.toString();
                } else {
                    a += "%5097%" + bodyData.toString();
                }
            }
        } catch (Exception e) {
        }
        return a;
    }


//    /**
//     * <p> The function is used to search subreddit's latest 10 submission using subreddit string passed in function paramter.</p>
//     *
//     * @author Parthiv Akbari
//     * @param V The string paramter is the name subreddit user of Reddit.
//     * @return the JSONObject is returned which contains the submissions of subreddit.
//     */
//    public JSONObject subredditAPI(String V){
//        JSONObject test = new JSONObject();
//        try{
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(subRedditAPI + URLEncoder.encode(V, "UTF-8") + "&size=10&fields=title,created_utc,author,subreddit&sort=DESC")).build();
//            res = client.send(req, HttpResponse.BodyHandlers.ofString());
//            Object obj = new JSONParser().parse(String.valueOf(res.body()));
//            test = (JSONObject) obj;
//        }catch (Exception e) {
//        }
//        return test;
//    }


//    /**
//     * The function is designed to get subreddit latest submission and to process JSON data.
//     *
//     * @author Parthiv Akabari
//     * @param key the Key value is the name of subreddit used to pass as function paramter.
//     * @return The list of <class>Subreddit</class> objets.
//     */
//    public List<subreddit> getSubredditData(String key) {
//        List<subreddit> ar = new ArrayList<subreddit>();
//        JSONArray array = (JSONArray) this.subredditAPI(key).get("data");
//        for (int i = 0; i < array.size(); i++) {
//            var temp = (JSONObject) array.get(i);
//            ar.add(new subreddit((String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit")));
//        }
//        return ar;
//    }
}