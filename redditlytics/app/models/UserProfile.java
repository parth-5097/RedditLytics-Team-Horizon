package models;

import play.mvc.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserProfile {

    String mainAPI = "https://api.pushshift.io/reddit/search/submission/?author=";
    HttpResponse res = null;
    List<String> l = new ArrayList<>();
    JSONObject bodyData = null;

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