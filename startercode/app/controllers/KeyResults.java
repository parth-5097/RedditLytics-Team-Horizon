package controllers;

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

public class KeyResults{

    private String mainAPI = "https://api.pushshift.io/reddit/search/comment/?q=";
    HttpResponse res= null;
    List<String> l= new ArrayList<>();
    JSONObject bodyData = null;
    public static void main(String[] args) {


    }

    public List<String> getData(String V){

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI+URLEncoder.encode(V,"UTF-8")+"&size=5&fields=body&sort=DESC")).build();
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            Object obj = new JSONParser().parse(String.valueOf(res.body()));
            JSONObject test = (JSONObject) obj;
            JSONArray array = (JSONArray) test.get("data");
            for (int i=0;i<array.size();i++) {
                bodyData = (JSONObject) array.get(i);
                String a = ((String) bodyData.get("body"));
                l.add(a);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return l;
    }

}