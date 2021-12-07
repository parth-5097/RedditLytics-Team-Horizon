package businesslogic;

import models.Wordcount;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;
import org.junit.BeforeClass;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

public class WordTest{

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