package controllers;

import play.mvc.*;
import models.*;
import java.util.*;
import java.util.concurrent.*;
/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private String key;
    private String data;
    KeyResults results = new KeyResults();
    Word word = new Word();
    UserProfile profile = new UserProfile();

    public HomeController() {
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
                .thenApply(i -> ok(i));
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> getWordStats(String a) {
        if(data != null && key==a){
            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        } else {
            this.key=a;
            data = results.getData(a);
            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        }

    }

    public CompletionStage<Result> getUserProfile(String username) {
//        List<UserData> userdata = profile.getData(username);
//        String author = userdata.get(0).getAuthor();
//        long totalAwardsReceived = userdata.get(0).getTotal_awards_received();
//
//        return ok(views.html.user_profile.render(userdata,author,totalAwardsReceived));
        return CompletableFuture
                .supplyAsync(() -> profile.getData(username))
                .thenApply(i ->  {
                    List<UserData> userdata = i;
                    System.out.println(userdata.get(0));
                    String author = userdata.get(0).getAuthor();
                    long totalAwardsReceived = userdata.get(0).getTotal_awards_received();
                    return ok(views.html.user_profile.render(userdata,author,totalAwardsReceived));
                });
    }

    public CompletionStage<Result> getSubreddit(String word){
        return CompletableFuture
                .supplyAsync(() -> results.getSubredditData(word))
                .thenApply(i -> ok(views.html.subreddit.render(i)));
    }

}