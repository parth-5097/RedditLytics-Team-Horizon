package actors;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.testkit.TestActorRef;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import akka.actor.*;

public class WebsocketactorTest extends JUnitRouteTest {

    private final ActorSystem system = ActorSystem.create();

    @Test
    public void testActorForsocket() {
        final Props props = Props.create(WebsocketActor.class);
        final ActorRef actorRef = system.actorOf(WebsocketActor.props(ActorRef.noSender()));
        final TestActorRef<WebsocketActor> ref = TestActorRef.create(system, props, "testA");
        final WebsocketActor actor = ref.underlyingActor();
        assertTrue(actor.getData("car").size()>0);
        assertTrue(actor.getData("car").size()>0);
        assertTrue(actor.getData("Apple").size()>0);
        assertTrue(actor.getData("car").size()>0);
        assertTrue(actor.getData("Science").size()>0);
    }
}