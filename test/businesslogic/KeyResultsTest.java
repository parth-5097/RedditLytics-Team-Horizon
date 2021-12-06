import businesslogic.KeyResults;
import models.subreddit;
import java.io.IOException;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
import java.util.*;
import org.junit.Before;
import org.junit.After;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.AbstractActor;
import java.util.stream.*;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import org.junit.ClassRule;
import akka.actor.typed.javadsl.Behaviors;
import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import java.time.Duration;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class KeyResultsTest extends Mockito{

    static ActorSystem actorSystem;
    static String temp = "Anything";

    public static class KeyResultsTestMock extends AbstractActor {
        String mainAPI = "http://localhost:0000";
        HttpResponse res = null;
        JSONObject bodyData = null;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(String.class, search -> {
                        String data = getData(search);
                        sender().tell(data, self());
                    })
                    .build();
        }

        public String getData(String V) {
            String a = V;
            return a;
        }

    }

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Mock
    private KeyResults keyResults;
    private ActorRef mainActor;

    @Before
    public void setup() {
        actorSystem = ActorSystem.create();
        keyResults = mock(KeyResults.class);
        when(keyResults.getData("Anything")).thenReturn(temp);
    }

    @Test
    public void testGetData() {
        final Props props = Props.create(KeyResultsTestMock.class);
        mainActor = actorSystem.actorOf(props);
        final TestKit testProbe = new TestKit(actorSystem);

        mainActor.tell("Anything",testProbe.getRef());
        String a = testProbe.expectMsgClass(String.class);
        assertEquals(temp,a);
    }

    @After
    public void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }
}