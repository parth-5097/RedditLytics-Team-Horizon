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
import views.html.index;

import javax.inject.Inject;

import java.time.Duration;

@Slf4j
public class HomeController extends Controller {
    private ActorSystem actorSystem;
    private Materializer materializer;

    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
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
              Source out = Source.tick(Duration.ofSeconds(1), Duration.ofSeconds(20), in)
                      .map(res -> res.toString());
              return Flow.fromSinkAndSource(in, out);
        });
    }
}
