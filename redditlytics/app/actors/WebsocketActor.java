package actors;

import akka.actor.*;
import businesslogic.SentimentAnalyzer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.URI;
import java.net.URLEncoder;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This socket actor will be called on every new user and maintain its result in jsonarray for each session.
 *
 * @author Group development
 */
public class WebsocketActor extends AbstractActor {
    String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";
    HttpResponse res = null;
    JSONArray result = new JSONArray();
    boolean match = false;

    public static Props props(ActorRef out) {
        return Props.create(WebsocketActor.class, out);
    }

    public WebsocketActor(){
    }

    private ActorRef out;

    public WebsocketActor(ActorRef out) {
        this.out = out;
    }

    /**
     * The functions trying to find out 250 latest submssions of the perticualr word using PushShift api.
     *
     * @author Group development
     * @param V The string parameter is used in PushShift api to search the results of that parameter.
     * @return The funcion returns the JSON String data.
     */
    public JSONArray getData(String V) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(V, "UTF-8") +
                    "&size=250&fields=title,selftext,created_utc,author,subreddit")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            JSONObject test = (JSONObject) obj;
            test.put("key", V);
            if (result.isEmpty()) {
                result.add(test);
            } else {
                result.add(test);
                match = false;
                result = getValuesForGivenKey(result, test, new JSONArray());
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * The functions help separates out duplicate results of same key when frequently fetching data using reddit pushshift api.
     *
     * @author Group development
     * @param jsonArrayStr Json array containing objects of results.
     * @param test Json object to be removed if found in array of objects.
     * @param result Json array object to be returned after processing with duplicates.
     * @return The funcion returns the JSON String data.
     */
    public JSONArray getValuesForGivenKey(JSONArray jsonArrayStr, JSONObject test, JSONArray result) {
        JSONObject obj = (JSONObject) jsonArrayStr.get(0);
        if (obj.get("key").toString().equals(test.get("key").toString()) && match == false) {
            match = true;
            jsonArrayStr.remove(0);
            result.add(test);
            if (jsonArrayStr.size() > 0) {
                getValuesForGivenKey(jsonArrayStr, test, result);
            } else {
                return result;
            }
        } else if (!obj.get("key").toString().equals(test.get("key").toString())) {
            jsonArrayStr.remove(0);
            result.add(obj);
            if (jsonArrayStr.size() > 0) {
                getValuesForGivenKey(jsonArrayStr, test, result);
            }
        } else {
            jsonArrayStr.remove(0);
            if (jsonArrayStr.size() > 0) {
                getValuesForGivenKey(jsonArrayStr, test, result);
            } else {
                return result;
            }
        }
        return result;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    out.tell(this.getData(message).toJSONString(), self());
                })
                .build();
    }

}