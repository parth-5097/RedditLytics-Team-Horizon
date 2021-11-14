import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import controllers.HomeController;

import org.junit.Test;

import play.mvc.Result;
import play.twirl.api.Content;

public class indexTest{

    @Test
    public void testIndexHtml(){
        Result result = new HomeController().index();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

}