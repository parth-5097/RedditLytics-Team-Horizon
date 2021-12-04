package controllers;
import actors.WebsocketActor;
import akka.actor.*;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import lombok.extern.slf4j.Slf4j;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import java.util.stream.*;
import views.html.index;
import play.cache.*;
import javax.inject.Inject;
import java.time.Duration;
import models.*;
import businesslogic.*;
import akka.Done;
import actors.*;
import java.util.*;
import java.util.concurrent.*;
import play.libs.Json;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;
import play.libs.streams.ActorFlow;
import actors.WebsocketActor;
import java.time.Duration;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import akka.actor.*;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;


@Slf4j
@Singleton
public class HomeController extends Controller {

    final ActorRef helloActor,wordActor,userprofileactor;

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
    }

    public Result index(Http.Request request) {
        String url = routes.HomeController.socket().webSocketURL(request);
        //To test WebSockets with akka streams, uncomment the next line and comment out the previous
//        String url = routes.HomeController.akkaStreamsSocket().webSocketURL(request);
        return ok(index.render(url));
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


//    public CompletionStage<Result> getSentimentResult(Http.Request request){
//        JsonNode json = request.body().asJson();
//        System.out.println(json.get("text"));
//        return CompletableFuture
//                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
//                .thenApply(i -> {
//                    return ok(String.valueOf(i));
//                });
//    }


    public CompletionStage<Result> getWordStats(String a) {

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


    public CompletionStage<Result> getSubreddit(String word) {

        return FutureConverters.toJava(ask(helloActor, new KeyResults.SubredditKey(word), Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.subreddit.render((List<subreddit>)response)));

    }
}