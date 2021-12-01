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
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import org.junit.ClassRule;
import akka.actor.typed.javadsl.Behaviors;
import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;


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
                final TestProbe probe = TestProbe.apply(actorSystem);
                final Props props = Props.create(Word.class, actorSystem);
                final ActorRef mainActor = actorSystem.actorOf(props);
                mainActor.tell(new Word.Key("Car"), getRef());

            }

        };

    }
}