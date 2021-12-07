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

//    @Test
//    public void testSentiment() {
//        try {
//            JsonNode jsonNode = (new ObjectMapper()).readTree("{ \"text\": \"This is team horizon testing!!\" }");
//            Http.RequestBuilder request = new Http.RequestBuilder()
//                    .method(POST)
//                    .bodyJson(jsonNode).uri("/sentiment");
//
//            Result result = route(app, request);
//            assertEquals(OK, result.status());
//        } catch (Exception e) {}
//
//    }

}
