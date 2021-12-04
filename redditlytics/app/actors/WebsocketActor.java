package actors;
import akka.actor.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.URI;
import java.net.URLEncoder;
import javax.inject.Inject;

import java.net.http.HttpClient;
import play.cache.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class WebsocketActor extends AbstractActor {
    String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";
    HttpResponse res = null;
    JSONArray result = new JSONArray();
    boolean match = false;

    public static Props props(ActorRef out) {
        return Props.create(WebsocketActor.class, out);
    }

    private final ActorRef out;

    public WebsocketActor(ActorRef out) {
        this.out = out;
    }

    public JSONArray getData(String V) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(V, "UTF-8") +
                    "&size=250&fields=title,selftext,created_utc,author,subreddit")).build();
            System.out.println("Client L"+client);
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
            System.out.println(e);
        }
        return result;
    }


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
            } else {
                return result;
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
                    String c = this.getData(message).toJSONString();
                    out.tell(this.getData(message).toJSONString(), self());
                })
                .build();
    }

}