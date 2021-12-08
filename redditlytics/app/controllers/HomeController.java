package controllers;

import actors.WebsocketActor;
import akka.actor.*;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import models.subreddit;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import views.html.*;
import play.cache.*;
import javax.inject.Inject;
import java.time.Duration;
import models.*;
import businesslogic.*;
import java.util.*;
import java.util.concurrent.*;
import javax.inject.Singleton;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 *
 * The Link to index page function named 'index':{@link index}
 * The Link to WebSocket function named 'socket':{@link socket}
 * The Link to WebSocket function named 'akkaStreamsSocket':{@link akkaStreamsSocket}
 * The Link to sentiment analysis function named 'getSentimentResult':{@link getSentimentResult}
 * The Link to word statistics function named 'getWordStats':{@link getWordStats}
 * The Link to search user profile function named 'getUserProfile':{@link getUserProfile}
 * The Link to search SubReddit submission function named 'getSubreddit':{@link getSubreddit}
 *
 * @author Group development
 */
@Slf4j
@Singleton
public class HomeController extends Controller {

    final ActorRef helloActor,wordActor,userprofileactor,subredditActor,sentimentActor;

    private String CacheKey = "Index";
    private AsyncCacheApi cache;
    String key;
    String data;
    private ActorSystem actorSystem;
    private Materializer materializer;


    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer,AsyncCacheApi cache) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.cache = cache;
        wordActor = actorSystem.actorOf(Word.getProps());
        helloActor = actorSystem.actorOf(KeyResults.getProps());
        userprofileactor = actorSystem.actorOf(UserProfile.getProps());
        subredditActor = actorSystem.actorOf(Subreddit.getProps());
        sentimentActor = actorSystem.actorOf(SentimentAnalyzer.getProps());
    }

    /**
     * <p>The function returns root file or index file.</p>
     *
     * <p>The function also maintains the cache during session. when session ends, cache will be deleted.</p>
     *
     * @author Group developemt
     * @return render index or Home page html file
     */
    public CompletionStage<Result> index(Http.Request request) {
        String url = routes.HomeController.socket().webSocketURL(request);
        cache.removeAll();
        return cache.set("key",UUID.randomUUID().toString()).thenApply(i -> request
                .session()
                .get("connected")
                .map(user -> ok(index.render(url)))
                .orElseGet(() -> ok(index.render(url))));
    }

    /**
     * <p>This function take request and create flow of actor reference.</p>
     *
     * @author Group developemt
     * @return And returns text in response
     */
    public WebSocket socket() {
        return WebSocket.Text.accept(out -> ActorFlow.actorRef(WebsocketActor::props, actorSystem,materializer));
    }

    /**
     * <p>This function process a same request continuously based on the given key from server until it receives another.</p>
     *
     * <p>And push the data to client side.</p>
     *
     * <p>This implementation can be regarded as reactive server-push paradigm.</p>
     *
     * @author Group developemt
     * @return And returns text in response
     */
    public WebSocket akkaStreamsSocket() {
        return WebSocket.Text.accept(
                request -> {
                    Sink in = Sink.foreach(out -> ActorFlow.actorRef(WebsocketActor::props, actorSystem,materializer));
                    Source out = Source.tick(Duration.ofSeconds(1), Duration.ofSeconds(7), in)
                            .map(res -> res.toString());
                    return Flow.fromSinkAndSource(in, out);
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
        return FutureConverters.toJava(ask(sentimentActor,String.valueOf(json.get("text")), Integer. MAX_VALUE))
                .thenApply(response ->  ok(String.valueOf(response)));
    }

    /**
     *  <p> The fucntion is designed to count word statistics on word passed in function parameter.</p>
     *
     * @author Jigar Borad
     * @param word the word string is used to find call api and perform word statistics on the result data.
     * @return the function returns html file contaiing the results of word statistics.
     */
    public CompletionStage<Result> getWordStats(String a) {
            return FutureConverters.toJava(ask(helloActor, a, Integer. MAX_VALUE))
                    .thenApply(response -> FutureConverters.toJava(ask(wordActor, new Word.Key((String)response), Integer. MAX_VALUE)))
                    .thenApply(response -> {
                        List<Wordcount> result = null;
                        try{
                            result = (List<Wordcount>) response.toCompletableFuture().get();
                        }catch(Exception e) {
                        }
                        return ok(word_stats.render(result));
                    });
    }

    /**
     * <p>The function is designed to search user profile.</p>
     *
     * @author Shubham Bhanderi
     * @param username parameter username is used to call api and find user profile and their submissions.
     * @return function returns the html file with userprofile data and their submissions.
     */
    public CompletionStage<Result> getUserProfile(String username) {
        return FutureConverters.toJava(ask(userprofileactor, username, Integer. MAX_VALUE))
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
                    return ok(user_profile.render(result, author, totalAwardsReceived));
                });
    }

    /**
     * <p> The function is developed to find subreddit submissions.</p>
     *
     * @author Parthiv Akbari
     * @param word The parameter is used to find subreddit profile and their submissions.
     * @return the html file which contains the subreddit submissions and profile.
     */
    public CompletionStage<Result> getSubreddit(String word) {
        return FutureConverters.toJava(ask(subredditActor, word, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.subreddit.render((List<subreddit>)response)));
    }

    /**
     * The function is used for caching the searched data. we are using play built in asynccache to cache the data using session key.
     * @param request http request to get data from.
     * @return done acknoledgement
     */
    public CompletionStage<Result> setCacheData(Http.Request request){
        JsonNode json = request.body().asJson();
        return CompletableFuture
                .supplyAsync(() -> cache.set(json.get("id").toString(),json.get("value")))
                .thenApply(i -> ok("Done"));
    }

    /**
     * the function us used to get cached id key to fetch the data
     * @return the cache id
     */
    public CompletionStage<Result> getCacheId(){
        return cache.get("key").thenApply(i -> ok(i.get().toString()));
    }

    /**
     * the function is used to fetch data from cache using key.
     * @param data is the key we used to fetch data from Asyncache
     * @return the data from cache memory
     */
    public CompletionStage<Result> getCacheData(String data){
        return cache.get(data).thenApply(i -> ok(i.get().toString()));
    }
}