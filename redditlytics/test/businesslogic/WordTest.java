package businesslogic;
import models.Wordcount;
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
import org.junit.BeforeClass;
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

public class WordTest{

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    static ActorSystem actorSystem;

    @BeforeClass
    public static void setup() {
        actorSystem = ActorSystem.create();
    }

    @Test
    public void testBodyData(){

        new TestKit(actorSystem){

            {
                final TestKit probe = new TestKit(actorSystem);
                String data = "{\"selftext\":\"This is jigar running run ran jigar\",\"author\":\"MiniLoona\",\"created_utc\":1636457205,\"title\":\"This is my outfit for our first date so you can breed me in your car before it even start\",\"subreddit\":\"BreedingMaterial\"}%5097%{\"selftext\":\"This is running run jigar\",\"author\":\"misscooke\",\"created_utc\":1636457199,\"title\":\"40% OFF ≡ƒÆª ≡ƒññ TOP 3% ≡ƒÿê 25 YEAR OLD PETITE CAR | ENDURO | MX GIRL ≡ƒÆÖ\",\"subreddit\":\"Blonde\"}";
                final ActorRef mainActor = actorSystem.actorOf(Word.getProps());
                mainActor.tell(new Word.Key(data), probe.getRef());
                List a = probe.expectMsgClass(List.class);
                Wordcount wc = (Wordcount)a.get(0);
                assertEquals("run",wc.getKey());
                assertEquals(5,wc.getValue());
                Wordcount wc1 = (Wordcount)a.get(1);
                assertEquals("jigar",wc1.getKey());
                assertEquals(3,wc1.getValue());
            }

        };

    }
}