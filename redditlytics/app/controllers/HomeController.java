package controllers;

import play.mvc.*;
import models.*;

import java.util.*;
import java.util.concurrent.*;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 *
 * @author Group Development
 */
public class HomeController extends Controller {
    private String key;
    private String data;
    KeyResults results = new KeyResults();
    NLP sa = new NLP();
    Word word = new Word();
    UserProfile profile = new UserProfile();

    public HomeController() {
        sa.init();
    }

    /**
     *
     *  The function uses the key value to call Pushshift api.
     *  then function returns the string result of api.
     *
     * @author Group Development
     *
     * @param key   The key value which is passed in POST url.
     *
     * @return The string value which will be used in   <code>index.scala.html</code> file to display result of search key.
     *
     */
    public CompletionStage<Result> getSearchResult(String key) {
        this.key = key;
        return CompletableFuture
                .supplyAsync(() -> results.getData(key))
                .thenApply(i -> ok(i));
    }

    /**
     * The
     *
     * @param request
     * @return
     */

    public CompletionStage<Result> getSentimentResult(Http.Request request){
        JsonNode json = request.body().asJson();
        return CompletableFuture
                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
                .thenApply(i -> {
                    return ok(String.valueOf(i));
                });
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> getWordStats(String a) {

        System.out.println("key :"+key+ " A :"+a);
        if (data != null && this.key == a) {

            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        } else {
            this.key = a;
            data = results.getData(a);
            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        }
    }

    public CompletionStage<Result> getUserProfile(String username) {
        return CompletableFuture
                .supplyAsync(() -> profile.getData(username))
                .thenApply(i -> {
                    List<UserData> userdata = i;
                    String author = userdata.get(0).getAuthor();
                    long totalAwardsReceived = userdata.get(0).getTotal_awards_received();
                    return ok(views.html.user_profile.render(userdata, author, totalAwardsReceived));
                });
    }

    public CompletionStage<Result> getSubreddit(String word) {
        return CompletableFuture
                .supplyAsync(() -> results.getSubredditData(word))
                .thenApply(i -> ok(views.html.subreddit.render(i)));
    }

}