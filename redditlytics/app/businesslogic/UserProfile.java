package businesslogic;

import models.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import akka.actor.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


/**
 * <p>The UserProfile class is designed to fetch user profile using PushShift Api.</p>
 *
 * @author Shubham Bhanderi
 */
public class UserProfile extends AbstractActor{

    public String mainAPI = "https://api.pushshift.io/reddit/search/submission/?author=";
    HttpResponse res = null;
    List<String> l = new ArrayList<>();
    JSONObject bodyData = null;

    public static Props getProps() {
        return Props.create(UserProfile.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, hello -> {
                    List<UserData> temp = getData(hello);
                    sender().tell(temp, self());
                })
                .build();
    }


    /**
     * <p>PushShift api call is made in the function and trying to fetch results.</p>
     *
     * @author Shubham Bhanderi
     * @param username the String parameter is used in api as parameter to search userprofile.
     * @return JSONObject is retured which contains the author data in json format.
     *
     */
    public JSONObject getUserData(String username){
        JSONObject test = new JSONObject();
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(username, "UTF-8") + "&fields=author_fullname,total_awards_received,title,created_utc,author,subreddit,score,upvote_ratio&size=10&sort=DESC")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            test = (JSONObject) obj;
        }catch (Exception e) {
        }
        return test;
    }

    /**
     * <p>the functions call api and fetch lated submissions of author and process the json data and returns the list of each data.</p>
     *
     * @author Shubham Bhanderi
     * @param username the String parameter is used to pass as parameter to another function.
     * @return the list of <class>UserData</class> objects which contains the userdata.
     */
    public List<UserData> getData(String username) {
        List<UserData> ar = new ArrayList<UserData>();
        JSONArray array = (JSONArray) this.getUserData(username).get("data");
        for (int i = 0; i < array.size(); i++) {
            var temp = (JSONObject) array.get(i);
            try{
                ar.add(new UserData((String) temp.get("author_fullname"), (Long) temp.get("total_awards_received"), (String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit"),(Long) temp.get("score"),(Double) temp.get("upvote_ratio")));
            }catch (Exception e){}
        }
        return ar;
    }
}