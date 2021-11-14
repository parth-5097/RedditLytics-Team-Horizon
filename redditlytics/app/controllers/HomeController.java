
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
 *
 * @author Group development
 *
 * The Link to index page function named 'index':{@link index}
 * The Link to word search function named 'getSearchResult':{@link getSearchResult}
 * The Link to sentiment analysis function named 'getSentimentResult':{@link getSentimentResult}
 * The Link to sentiment analysis function named 'getSentimentResult':{@link getSentimentResult}
 * The Link to word statistics function named 'getWordStats':{@link getWordStats}
 * The Link to search user profile function named 'getUserProfile':{@link getUserProfile}
 * The Link to search subrediit submission function named 'getSubreddit':{@link getSubreddit}
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
    public HomeController(){

    }

    @Inject
    public HomeController(AsyncCacheApi cache) {
        sa.init();
        this.cache = cache;
    }

    /**
     *
     *  <p>The function uses the key value to call Pushshift api.
     *  then function returns the string result of api.</p>
     *
     * <p>The function also set cache value gor every unique results.</p>
     *
     *
     * @author Group Development
     * @param key   The key value which is passed in POST url.
     * @return The string value which will be used in <b>index html </b> file to display result of search key.
     *
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


    /**
     *
     * <p>The function is designed to perform sentiment analysis on post request data using stanford coreNLP lib.</p>
     *
     * <p>The function is called when <b>/sentiment</b> url get triggered.</p>
     *
     * @author Group development
     * @param request post request contains json data which will be used in sentiment analysis.
     * @return sentiment result of data.
     */

    public CompletionStage<Result> getSentimentResult(Http.Request request){
        JsonNode json = request.body().asJson();
        System.out.println(json.get("text"));
        return CompletableFuture
                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
                .thenApply(i -> {
                    return ok(String.valueOf(i));
                });
    }


    /**
     * <p>The function returns root file or index file.</p>
     *
     * <p>The function also maintains the cache during session. when session ends, cache will be deleted.</p>
     *
     * @author Group developemt
     * @return render index or Home page html file
     */

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


    /**
     *  <p> The fucntion is designed to count word statistics on word passed in function parameter.</p>
     *
     * @author Jigar Borad
     * @param word the word string is used to find call api and perform word statistics on the result data.
     * @return the function returns html file contaiing the results of word statistics.
     */

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

    /**
     * <p>The function is designed to search user profile.</p>
     *
     * @author Shubham Bhanderi
     * @param username parameter username is used to call api and find user profile and their submissions.
     * @return function returns the html file with userprofile data and their submissions.
     */

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


    /**
     * <p> The function is developed to find subreddit submissions.</p>
     *
     * @param word The parameter is used to find subreddit profile and their submissions.
     * @return the html file which contains the subreddit submissions and profile.
     */
    public CompletionStage<Result> getSubreddit(String word) {
        return CompletableFuture
                .supplyAsync(() -> results.getSubredditData(word))
                .thenApply(i -> ok(views.html.subreddit.render(i)));
    }
}

