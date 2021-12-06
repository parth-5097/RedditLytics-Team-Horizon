import businesslogic.Subreddit;
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


public class SubredditTest extends Mockito{

    static ActorSystem actorSystem;
    static List<subreddit> l;

    public static class SubredditTestMock extends AbstractActor {
        String subRedditAPI = "http://localhost:0000";
        HttpResponse res = null;
        JSONObject bodyData = null;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(String.class, subreddit -> {
                        List<subreddit> data = l;
                        sender().tell(data, self());
                    })
                    .build();
        }

        public JSONObject subredditAPI(String V){
            JSONObject test = new JSONObject();
            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(subRedditAPI + URLEncoder.encode(V, "UTF-8") + "&size=10&fields=title,created_utc,author,subreddit&sort=DESC")).build();
                res = client.send(req, HttpResponse.BodyHandlers.ofString());
                Object obj = new JSONParser().parse(String.valueOf(res.body()));
                test = (JSONObject) obj;
            }catch (Exception e) {
            }
            return test;
        }

        public List<subreddit> getSubredditData(String key) {
            List<subreddit> ar = new ArrayList<subreddit>();
            JSONArray array = (JSONArray) this.subredditAPI(key).get("data");
            for (int i = 0; i < array.size(); i++) {
                var temp = (JSONObject) array.get(i);
                ar.add(new subreddit((String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit")));
            }
            return ar;
        }

    }

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Mock
    private Subreddit subreddit;
    private ActorRef mainActor;

    @Before
    public void setup() {
        actorSystem = ActorSystem.create();
        subreddit = mock(Subreddit.class);
        l = new ArrayList<subreddit>();
        l.add(new subreddit("1",2L,"Shubham","Bhanderi"));
        when(subreddit.getSubredditData("qassym")).thenReturn(l);
    }

    @Test
    public void testGetData() {
        final Props props = Props.create(SubredditTestMock.class);
        mainActor = actorSystem.actorOf(props);
        final TestKit testProbe = new TestKit(actorSystem);

        mainActor.tell("qassym",testProbe.getRef());
        List a = testProbe.expectMsgClass(List.class);
        assertEquals(l,a);
    }

    @After
    public void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }
}