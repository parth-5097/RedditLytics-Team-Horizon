import businesslogic.UserProfile;
import models.UserData;
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


public class UserprofileTest extends Mockito{

    static ActorSystem actorSystem;
    static List<UserData> l;

    public static class UserProfileTestMock extends AbstractActor {
        public String mainAPI = "http://localhost:0000";
        HttpResponse res = null;
        JSONObject bodyData = null;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(String.class, hello -> {
                        System.out.println(hello);
                        List<UserData> temp = l;
                        System.out.println(temp);
                        sender().tell(temp, self());
                    })
                    .build();
        }

        public JSONObject getUserData(String username){
            JSONObject test = new JSONObject();
            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(mainAPI + URLEncoder.encode(username, "UTF-8") + "&fields=author_fullname,total_awards_received,title,created_utc,author,subreddit,score,upvote_ratio&size=10&sort=DESC")).build();
                res = client.send(req, HttpResponse.BodyHandlers.ofString());
                Object obj = new JSONParser().parse(String.valueOf(res.body()));
                test = (JSONObject) obj;
            }catch (Exception e) {
            }
            return test;
        }

        public List<UserData> getData(String username) {
            List<UserData> ar = new ArrayList<UserData>();
            JSONArray array = (JSONArray) this.getUserData(username).get("data");
            for (int i = 0; i < array.size(); i++) {
                var temp = (JSONObject) array.get(i);
                try{
                    ar.add(new UserData((String) temp.get("author_fullname"), (Long) temp.get("total_awards_received"), (String) temp.get("author"), (Long) temp.get("created_utc"), (String) temp.get("title"), (String) temp.get("subreddit"),(Long) temp.get("score"),(Double) temp.get("upvote_ratio")));
                }catch (Exception e){}
            }
            return ar;
        }
    }

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Mock
    private UserProfile userProfile;
    private ActorRef mainActor;

    @Before
    public void setup() {
        actorSystem = ActorSystem.create();
        userProfile = mock(UserProfile.class);
        l = new ArrayList<UserData>();
        l.add(new UserData("1",2L,"Shubham",3L,"Bhanderi","Jigar",5L,6.0));
        when(userProfile.getData("qassym")).thenReturn(l);
    }

    @Test
    public void testGetData(){
        final Props props = Props.create(UserProfileTestMock.class);
        mainActor = actorSystem.actorOf(props);

        final TestKit testProbe = new TestKit(actorSystem);
        mainActor.tell("qassym",testProbe.getRef());
        List a = testProbe.expectMsgClass(List.class);
        System.out.println("abc" + a);
        assertEquals(l,a);
    }

    @After
    public void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }
}