package models;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
public class WordCountTest {

    Wordcount test = new Wordcount("Jigar",22);
    @Test
    public void getKeyTest() {
        assertEquals("Jigar",test.getKey());
    }

    @Test
    public void getValueTest() {
        assertEquals(22,test.getValue());
    }
}