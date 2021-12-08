package businesslogic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
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
import java.net.http.HttpResponse;
import org.json.simple.*;

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