package controllers;

import play.mvc.*;

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

public class KeyResultsController {

    private String mainAPI = "https://api.pushshift.io/reddit/search/submission/?q=";
    private String subRedditAPI = "https://api.pushshift.io/reddit/search/submission/?subreddit=";
    HttpResponse res = null;
    List<String> l = new ArrayList<>();
    JSONObject bodyData = null;

    public String getData(String V) {
        String a = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(V, "UTF-8") + "&size=10&fields=title,created_utc,author,subreddit&sort=DESC")).build();
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
            e.printStackTrace();
        }
        return a;
    }

    public List<subreddit> getSubredditData(String V) {
        List<subreddit> ar = new ArrayList<subreddit>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(subRedditAPI + URLEncoder.encode(V, "UTF-8") + "&size=10&fields=title,created_utc,author,subreddit&sort=DESC")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            JSONObject test = (JSONObject) obj;
            JSONArray array = (JSONArray) test.get("data");
            for (int i = 0; i < array.size(); i++) {
                var temp = (JSONObject) array.get(i);
                ar.add(new subreddit((String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ar;
    }
}