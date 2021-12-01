package businesslogic;

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
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import org.junit.ClassRule;
import akka.actor.typed.javadsl.Behaviors;
import static org.junit.Assert.*;

public class WordTest{


    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();


    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @Test
    public void testBodyData(){

        new TestKit(system) {
            final Props props = Props.create(Word.class);
            final ActorRef testProbe = system.actorOf(props);
            final TestKit probe = new TestKit(system);

                 testProbe.tell(new Word.Key("{\\\"selftext\\\":\\\"This is jigar running run ran jigar\\\",\\\"author\\\":\\\"MiniLoona\\\",\\\"created_utc\\\":1636457205,\\\"title\\\":\\\"This is my outfit for our first date so you can breed me in your car before it even start\\\",\\\"subreddit\\\":\\\"BreedingMaterial\\\"}%5097%{\\\"selftext\\\":\\\"This is running run jigar\\\",\\\"author\\\":\\\"misscooke\\\",\\\"created_utc\\\":1636457199,\\\"title\\\":\\\"40% OFF ≡ƒÆª ≡ƒññ TOP 3% ≡ƒÿê 25 YEAR OLD PETITE CAR | ENDURO | MX GIRL ≡ƒÆÖ\\\",\\\"subreddit\\\":\\\"Blonde\\"),getRef());
                 expectMsg(Duration.ZERO, "world");
        };



//        assertEquals("jigar",wordCount.get(1).getKey());
//        assertEquals(3,wordCount.get(1).getValue());
//        assertEquals("run",wordCount.get(0).getKey());
//        assertEquals(5,wordCount.get(0).getValue());
//        List<String> a = wordCount.stream().map(o -> o.getKey()).collect(Collectors.toList());
//        System.out.println(a);
    }

//    @Test
//    public void testCatchBodyData() throws IOException{
//        Word w = new Word();
//        w.bodyData("jdnkjcdjncjdnkjcnkd");
//    }
}