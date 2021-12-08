package businesslogic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;
import org.junit.Before;
import org.junit.After;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.testkit.javadsl.TestKit;
import org.junit.ClassRule;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;

public class SentimentAnalyzerTest {

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
                String data = "You’re off to great places, today is your day. Your mountain is waiting, so get on your way.";
                final ActorRef mainActor = actorSystem.actorOf(SentimentAnalyzer.getProps());
                mainActor.tell(data, probe.getRef());

            }

        };

    }

}