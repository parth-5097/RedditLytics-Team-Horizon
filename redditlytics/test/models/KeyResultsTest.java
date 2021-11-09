package models;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.Before;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import java.net.URI;
import java.net.URLEncoder;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import org.mockito.Mock;

public class KeyResultsTest extends Mockito{
    private DataSaver d;

    @Before
    public void ResultString(){
        KeyResults keyResults  = mock(KeyResults.class);

        when(keyResults.getData("apple")).thenReturn("Hello bsdk rodrigo");

        d = new DataSaver(keyResults);
    }

    @Test
    public void rodrigoBC(){
        assertEquals(d.getAndSaveData(),"Hello bsdk rodrigo");
    }
}