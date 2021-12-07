package businesslogic;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
 */

public class KeyResults extends AbstractActor{
    String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";
    HttpResponse res = null;
    JSONObject bodyData = null;

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
}