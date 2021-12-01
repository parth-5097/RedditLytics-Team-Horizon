package controllers;
import play.mvc.*;
import models.*;
import businesslogic.*;
import akka.Done;
import java.util.*;
import java.util.concurrent.*;
import play.libs.Json;
import play.cache.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import akka.actor.*;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 *
 * The Link to index page function named 'index':{@link index}
 * The Link to word search function named 'getSearchResult':{@link getSearchResult}
 * The Link to sentiment analysis function named 'getSentimentResult':{@link getSentimentResult}
 * The Link to sentiment analysis function named 'getSentimentResult':{@link getSentimentResult}
 * The Link to word statistics function named 'getWordStats':{@link getWordStats}
 * The Link to search user profile function named 'getUserProfile':{@link getUserProfile}
 * The Link to search subrediit submission function named 'getSubreddit':{@link getSubreddit}
 *
 * @author Group development
 */

@Singleton
public class HomeController extends Controller {

    final ActorRef helloActor,wordActor,userprofileactor;
    private AsyncCacheApi cache;
    private String CacheKey = "Index";
    String key;
    String data;
    SentimentAnalyzer sa = new SentimentAnalyzer();


    @Inject
    public HomeController(ActorSystem system) {
        wordActor = system.actorOf(Word.getProps());
        helloActor = system.actorOf(KeyResults.getProps());
        userprofileactor = system.actorOf(UserProfile.getProps());
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
        return FutureConverters.toJava(ask(helloActor, new KeyResults.Key(key), Integer. MAX_VALUE))
                .thenApply(response ->ok((String) response));
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
//
//    public CompletionStage<Result> getSentimentResult(Http.Request request){
//        JsonNode json = request.body().asJson();
//        System.out.println(json.get("text"));
//        return CompletableFuture
//                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
//                .thenApply(i -> {
//                    return ok(String.valueOf(i));
//                });
//    }


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
            return FutureConverters.toJava(ask(wordActor, new Word.Key(data), Integer. MAX_VALUE))
                    .thenApply(response ->ok(views.html.word_stats.render((List<Wordcount>) response)));
        } else {
            this.key = a;
            return FutureConverters.toJava(ask(helloActor, new KeyResults.Key(a), Integer. MAX_VALUE))
                    .thenApply(response -> FutureConverters.toJava(ask(wordActor, new Word.Key((String)response), Integer. MAX_VALUE)))
                    .thenApply(response -> {
                        List<Wordcount> result = null;
                        try{
                            result = (List<Wordcount>) response.toCompletableFuture().get();
                        }catch(Exception e) {
                        }
                        return ok(views.html.word_stats.render(result));
                    });
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
        return FutureConverters.toJava(ask(userprofileactor, new UserProfile.AuthorKey(username), Integer. MAX_VALUE))
                .thenApply(response -> {
                    List<UserData> result = null;
                    String author=null;
                    long totalAwardsReceived=0L;
                    try{
                        result = (List<UserData>) response;
                        author = result.get(0).getAuthor();
                        totalAwardsReceived = result.get(0).getTotal_awards_received();
                    }catch(Exception e) {
                    }
                    return ok(views.html.user_profile.render(result, author, totalAwardsReceived));
                });
    }

    /**
     * <p> The function is developed to find subreddit submissions.</p>
     *
     * @param word The parameter is used to find subreddit profile and their submissions.
     * @return the html file which contains the subreddit submissions and profile.
     */
    public CompletionStage<Result> getSubreddit(String word) {

        return FutureConverters.toJava(ask(helloActor, new KeyResults.SubredditKey(word), Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.subreddit.render((List<subreddit>)response)));

    }

}

