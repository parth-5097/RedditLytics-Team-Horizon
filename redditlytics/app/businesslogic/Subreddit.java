package businesslogic;

import models.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import akka.actor.*;

/**
 *
 * <p>The class is defined to call PushShift api.</p>
 *
 * <p>The class has 3 methods. A) getData(string) b)subredditAPI(string) c)getSubredditData(string) </p>
 * <p>The methods are used to call api to search data of specific word or to search latest submission of subreddit</p>
 *
 * @author Parthiv Akabari
 */

public class Subreddit extends AbstractActor{
    String subRedditAPI = "https://api.pushshift.io/reddit/search/submission/?subreddit=";
    HttpResponse res = null;
    JSONObject bodyData = null;

    public static Props getProps() {
        return Props.create(Subreddit.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, subreddit -> {
                    List<subreddit> data = getSubredditData(subreddit);
                    sender().tell(data, self());
                })
                .build();
    }

    /**
     * <p> The function is used to search subreddit's latest 10 submission using subreddit string passed in function paramter.</p>
     *
     * @author Parthiv Akbari
     * @param V The string paramter is the name subreddit user of Reddit.
     * @return the JSONObject is returned which contains the submissions of subreddit.
     */
    public JSONObject subredditAPI(String V){
        JSONObject test = new JSONObject();
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(subRedditAPI + URLEncoder.encode(V, "UTF-8") + "&size=10&fields=title,created_utc,author,subreddit&sort=DESC")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            test = (JSONObject) obj;
        }catch (Exception e) {
        }
        return test;
    }


    /**
     * The function is designed to get subreddit latest submission and to process JSON data.
     *
     * @author Parthiv Akabari
     * @param key the Key value is the name of subreddit used to pass as function paramter.
     * @return The list of <class>Subreddit</class> objets.
     */
    public List<subreddit> getSubredditData(String key) {
        List<subreddit> ar = new ArrayList<subreddit>();
        JSONArray array = (JSONArray) this.subredditAPI(key).get("data");
        for (int i = 0; i < array.size(); i++) {
            var temp = (JSONObject) array.get(i);
            ar.add(new subreddit((String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit")));
        }
        return ar;
    }
}