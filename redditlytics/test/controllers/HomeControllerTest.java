package controllers;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;
public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testRoutesAssets() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/assets/images/Searchs_004.png");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testWordStat() {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/search/apple");
        Result result = route(app, request);
        assertEquals(OK, result.status());


        Http.RequestBuilder request1 = new Http.RequestBuilder()
                .method(GET)
                .uri("/search/apple");

        Result result1 = route(app, request1);
        assertEquals(OK, result1.status());


    }


    @Test
    public void testSubReddit() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/subreddit/gun");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testUserProfile() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/userprofile/jreddit4321");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }


    @Test
    public void testsentiment() {
        try {
            JsonNode jsonNode = (new ObjectMapper()).readTree("{ \"value\": \"This is team horizon testing!!\"}");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .bodyJson(jsonNode).uri("/sentiment");

            Result result = route(app, request);
            assertEquals(OK, result.status());
        } catch (Exception e) {}
    }

        @Test
     public void testsetCache() {
        try {
            JsonNode jsonNode = (new ObjectMapper()).readTree("{ \"value\": \"This is team horizon testing!!\",\"id\": \"99999\" }");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .bodyJson(jsonNode).uri("/setCache");

            Result result = route(app, request);
            assertEquals(OK, result.status());

            Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/getCacheId");

            Result result1 = route(app, request1);
            assertEquals(OK, result1.status());

            Http.RequestBuilder request2 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/getCache/99999");

            Result result2 = route(app, request2);
            assertEquals(OK, result2.status());


        } catch (Exception e) {}

    }

}
