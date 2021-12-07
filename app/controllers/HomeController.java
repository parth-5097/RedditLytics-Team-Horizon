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


@Slf4j
@Singleton
public class HomeController extends Controller {

    final ActorRef helloActor,wordActor,userprofileactor,subredditActor;

    private String CacheKey = "Index";
    private AsyncCacheApi cache;
    String key;
    String data;
    SentimentAnalyzer sa = new SentimentAnalyzer();
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
    }

    public CompletionStage<Result> index(Http.Request request) {
        String url = routes.HomeController.socket().webSocketURL(request);
        cache.removeAll();
        return cache.set("key",UUID.randomUUID().toString()).thenApply(i -> request
                .session()
                .get("connected")
                .map(user -> ok(index.render(url)))
                .orElseGet(() -> ok(index.render(url))));
    }

    public WebSocket socket() {
        return WebSocket.Text.accept(out -> ActorFlow.actorRef(WebsocketActor::props, actorSystem,materializer));
    }

    public WebSocket akkaStreamsSocket() {
        return WebSocket.Text.accept(
                request -> {
                    Sink in = Sink.foreach(out -> ActorFlow.actorRef(WebsocketActor::props, actorSystem,materializer));
                    Source out = Source.tick(Duration.ofSeconds(1), Duration.ofSeconds(15), in)
                            .map(res -> res.toString());
                    return Flow.fromSinkAndSource(in, out);
                });
    }


    public CompletionStage<Result> getSentimentResult(Http.Request request){
        JsonNode json = request.body().asJson();
        return CompletableFuture
                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
                .thenApply(i -> ok(String.valueOf(i)));
    }

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

    public CompletionStage<Result> getSubreddit(String word) {
        return FutureConverters.toJava(ask(subredditActor, word, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.subreddit.render((List<subreddit>)response)));
    }

    public CompletionStage<Result> setCacheData(Http.Request request){
        JsonNode json = request.body().asJson();
        return CompletableFuture
                .supplyAsync(() -> cache.set(json.get("id").toString(),json.get("value")))
                .thenApply(i -> ok("Done"));
    }

    public CompletionStage<Result> getCacheId(){
        return cache.get("key").thenApply(i -> ok(i.get().toString()));
    }

    public CompletionStage<Result> getCacheData(String data){
        return cache.get(data).thenApply(i -> ok(i.get().toString()));
    }
}