package controllers;

import play.mvc.*;
import models.*;
import akka.Done;

import java.util.*;
import java.util.concurrent.*;
import play.libs.Json;

import play.cache.*;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private AsyncCacheApi cache;
    private String CacheKey = "Index";
    String key;
    String data;
    KeyResults results = new KeyResults();
    SentimentAnalyzer sa = new SentimentAnalyzer();
    Word word = new Word();
    UserProfile profile = new UserProfile();

    public HomeController(){}

    @Inject
    public HomeController(AsyncCacheApi cache) {
        sa.init();
        this.cache = cache;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public CompletionStage<Result> getSearchResult(String key) {
        this.key = key;
        return CompletableFuture
                .supplyAsync(() -> results.getData(key))
                .thenApply(i -> {
                    CompletionStage<Done> result = cache.set(CacheKey, i);
                    return ok(i);
                });
    }

    public CompletionStage<Result> getSentimentResult(Http.Request request){
        JsonNode json = request.body().asJson();
        String tweet = String.valueOf(json.get("text"));
        return CompletableFuture
                .supplyAsync(() -> sa.findSentiment(tweet))
                .thenApply(i -> {
                    return ok(String.valueOf(i));
                });
    }

    public CompletionStage<Result> index() {
        return CompletableFuture
                .supplyAsync(() -> {
                    CompletionStage<Optional<Object>> data = cache.get(CacheKey);
                    return data;
                })
                .thenApply(i -> {
                    return ok(views.html.index.render());
                });
    }

    public CompletionStage<Result> getWordStats(String a) {
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